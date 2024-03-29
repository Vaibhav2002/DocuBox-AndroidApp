package com.docubox.ui.screens.main.documents

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.local.models.FileOption
import com.docubox.data.local.models.FolderOptions
import com.docubox.data.local.models.StorageItem
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.databinding.SheetUploadDocumentBinding
import com.docubox.service.FileUploadService
import com.docubox.ui.adapter.OneAdapter
import com.docubox.util.Constants.fileOptions
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val binding by viewBinding(FragmentDocumentsBinding::bind)
    private val viewModel by viewModels<DocumentsViewModel>()
    private lateinit var storageAdapter: OneAdapter<StorageItem, ItemStorageBinding>
    private lateinit var filePickerLauncher: ActivityResultLauncher<String>

    private var isBound = false
    private lateinit var fileUploadService: FileUploadService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder) {
            isBound = true
            fileUploadService = (p1 as FileUploadService.FileUploadBinder).getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initViews()
        initListeners()
        collectUiState()
        collectEvents()
        onBackPress(viewModel::onBackPress)
        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let { uploadFile(it) }
            }
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), FileUploadService::class.java).also {
            requireContext().bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unbindService(connection)
        isBound = false
    }

    private fun collectEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            is DocumentsScreenEvents.ShowToast -> requireContext().showToast(it.message)
            DocumentsScreenEvents.NavigateBack -> findNavController().popBackStack()
        }
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar(
            title = DocumentsViewModel.ROOT_FOLDER_NAME,
            backButtonEnabled = true,
            backButtonOnClickListener = viewModel::onBackPress
        )
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        storageAdapter.submitList(it.storageItems.sortStorageItems())
        binding.apply {
            actionBar.tvActionBarTitle.text = it.actionBarTitle
            swipeRefresh.isRefreshing = it.isRefreshing
            emptyStateLayout.emptyStateLayout.visibleOrGone(it.storageItems.isEmpty() && !it.isLoading)
            progressLayout.visibleOrGone(it.isLoading)
        }
    }

    private fun initViews() = with(binding) {
        fileAndFolderRv.setHasFixedSize(false)
        storageAdapter = fileAndFolderRv.compose(
            ItemStorageBinding::inflate,
            onBind = { item: StorageItem, pos ->
                title.text = item.name
                description.text = item.description
                itemImage.setImageResource(item.icon)
                root.setOnLongClickListener {
                    when (item) {
                        is StorageItem.File -> handleFileLongPress(item)
                        is StorageItem.Folder -> handleFolderLongPress(item)
                    }
                    true
                }
            },
        ) {
            handleStorageItemPress(this)
        }
    }

    private fun initListeners() = with(binding) {
        addFileBtn.singleClick(this@DocumentsFragment::openBottomSheet)
        swipeRefresh.setOnRefreshListener(viewModel::onRefresh)
    }

    private fun handleStorageItemPress(item: StorageItem) {
        when (item) {
            is StorageItem.Folder -> viewModel.onFolderPress(item)
            is StorageItem.File -> {
                val action =
                    DocumentsFragmentDirections.actionDocumentsFragmentToViewDocumentFragment(item)
                findNavController().navigate(action)
            }
        }
    }

    private fun openBottomSheet() {

        val bottomSheetBinding =
            SheetUploadDocumentBinding.inflate(LayoutInflater.from(requireContext()))

        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            R.style.DocuBox_BottomSheet
        ).apply {
            setContentView(bottomSheetBinding.root)
            bottomSheetBinding.btnUploadFile.singleClick {
                dismiss()
                openFilePicker()
            }
            bottomSheetBinding.btnCreateFolder.singleClick {
                dismiss()
                handleCreateFolder()
            }
        }.also {
            it.show()
        }

    }

    private fun handleCreateFolder() = lifecycleScope.launchWhenStarted {
        requireContext().showInputDialog(
            "Enter Folder name",
            "",
            "Enter folder name",
            "Folder name"
        ).also {
            if (it.isEmpty()) return@also
            viewModel.createFolder(it)
        }
    }

    private fun openFilePicker() = lifecycleScope.launchWhenStarted {
        askStoragePermission().also {
            if (it) filePickerLauncher.launch("*/*")
        }
    }

    private fun uploadFile(file: Uri) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        if (!isBound) return@launchWhenStarted
        val storageLeft = viewModel.getStorageLeft()
        if (storageLeft == null) {
            requireContext().showToast("Cannot upload file, failed to get storage Available")
            return@launchWhenStarted
        }
        fileUploadService.uploadFile(
            file = file,
            fileDirectory = viewModel.getCurrentDirectory(),
            storageLeft = storageLeft.MbToBytes(),
            token = viewModel.userToken,
            lifeCycleOwner = this@DocumentsFragment
        ).also {
            if (it) viewModel.getData()
        }
    }

    private fun handleFileLongPress(file: StorageItem.File) {
        val options = fileOptions.toMutableList()
            .apply {
                if (file.file.fileSharedTo.isEmpty()) remove(FileOption.RevokeShare)
            }
        showFileOptions(
            file = file,
            options = options,
            onShare = viewModel::shareFile,
            onDelete = viewModel::deleteFile,
            onRevokeShare = viewModel::revokeShareFile,
            onDownload = viewModel::downloadFile,
            onRename = viewModel::renameFile
        )
    }

    private fun handleFolderLongPress(folder: StorageItem.Folder) {
        val options = listOf(FolderOptions.Rename, FolderOptions.Delete)
        showFolderOptions(
            folder = folder,
            options = options,
            onDelete = viewModel::deleteFolder,
            onRename = viewModel::renameFolder
        )
    }
}
