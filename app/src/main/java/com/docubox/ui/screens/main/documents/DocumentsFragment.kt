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
import com.docubox.data.modes.local.FileOption
import com.docubox.data.modes.local.FolderOptions
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.databinding.SheetUploadDocumentBinding
import com.docubox.service.FileUploadService
import com.docubox.ui.adapter.OneAdapter
import com.docubox.ui.screens.dialogs.FileOptionsBottomSheetFragment
import com.docubox.ui.screens.dialogs.FolderOptionsBottomSheetFragment
import com.docubox.util.Constants.FILE_OPTION_DIALOG
import com.docubox.util.Constants.FOLDER_OPTION_DIALOG
import com.docubox.util.Constants.fileOptions
import com.docubox.util.Constants.folderOptions
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            uploadFile(it)
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
        storageAdapter.submitList(it.storageItems)
        binding.actionBar.tvActionBarTitle.text = it.actionBarTitle
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
    }

    private fun handleStorageItemPress(item: StorageItem) {
        when (item) {
            is StorageItem.Folder -> viewModel.onFolderPress(item)
            is StorageItem.File -> Unit
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
        fileUploadService.uploadFile(file, viewModel.getCurrentDirectory(), viewModel.userToken)
            .also {
                Timber.d(if (it) "File Uploaded" else "Failed to upload")
            }
    }

    private fun handleFileLongPress(file: StorageItem.File) {
        FileOptionsBottomSheetFragment(fileOptions) {
            when (it) {
                FileOption.Delete -> Timber.d("Delete File")
                FileOption.Rename -> Timber.d("Rename File")
                FileOption.RevokeShare -> Timber.d("Revoke File Share")
                FileOption.Share -> handleShareFile(file)
            }
        }.show(childFragmentManager, FILE_OPTION_DIALOG)
    }

    private fun handleFolderLongPress(folder: StorageItem.Folder) {
        FolderOptionsBottomSheetFragment(folderOptions) {
            when (it) {
                FolderOptions.Delete -> Timber.d("Delete Folder")
                FolderOptions.Rename -> Timber.d("Rename Folder")
                FolderOptions.RevokeShare -> Unit
                FolderOptions.Share -> Unit
            }
        }.show(childFragmentManager, FOLDER_OPTION_DIALOG)
    }

    private fun handleShareFile(file: StorageItem.File) =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            requireContext().showInputDialog(
                title = "Enter email",
                placeholder = "Enter email",
                label = "Email"
            ).also {
                if (it.isEmpty()) return@also
                viewModel.shareFile(file, it)
            }
        }
}
