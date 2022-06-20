package com.docubox.ui.screens.main.shared

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.modes.local.FileOption
import com.docubox.data.modes.local.FolderOptions
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentSharedBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.ui.adapter.OneAdapter
import com.docubox.ui.screens.dialogs.FileOptionsBottomSheetFragment
import com.docubox.util.Constants
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharedFragment : Fragment(R.layout.fragment_shared) {

    private val viewModel by viewModels<SharedViewModel>()
    private val binding by viewBinding(FragmentSharedBinding::bind)
    private lateinit var storageAdapter: OneAdapter<StorageItem, ItemStorageBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initViews()
        initListeners()
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        storageAdapter.submitList(it.storageItems)
        binding.apply {
            btnSharedToMe.setSelectedState(!it.isSharedByMeState)
            btnSharedByMe.setSelectedState(it.isSharedByMeState)
            swipeRefresh.isRefreshing = it.isRefreshing
        }
    }

    private fun collectUiEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            is SharedScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }

    private fun initListeners() = with(binding) {
        btnSharedByMe.singleClick(viewModel::onSharedByMeButtonPress)
        btnSharedToMe.singleClick(viewModel::onSharedToMeButtonPress)
        swipeRefresh.setOnRefreshListener(viewModel::onRefresh)
    }

    private fun initViews() = with(binding) {
        storageRv.setHasFixedSize(false)
        storageAdapter = storageRv.compose(
            ItemStorageBinding::inflate,
            onBind = { item: StorageItem, _ ->
                title.text = item.name
                description.text = item.description
                itemImage.setImageResource(item.icon)
                root.setOnLongClickListener {
                    if (!viewModel.uiState.value.isSharedByMeState) true
                    else {
                        when (item) {
                            is StorageItem.File -> handleFileLongPress(item)
                            is StorageItem.Folder -> Unit
                        }
                        true
                    }
                }
            },
        ) {
        }
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Shared", true, {
            findNavController().popBackStack()
        })
    }

    private fun handleFileLongPress(file: StorageItem.File) {
        FileOptionsBottomSheetFragment(listOf(FileOption.RevokeShare, FileOption.Delete)) {
            when (it) {
                FileOption.Delete -> handleDeleteFile(file)
                FileOption.Rename -> Unit
                FileOption.RevokeShare -> handleRevokeShareFile(file)
                FileOption.Share -> Unit
            }
        }.show(childFragmentManager, Constants.FILE_OPTION_DIALOG)
    }

    private fun handleRevokeShareFile(file: StorageItem.File) =
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            requireContext().showSelectItemDialog(
                title = "Select User",
                items = file.file.fileSharedTo
            ).also { email ->
                email?.let { viewModel.revokeShareFile(file, it) }
            }
        }
    private fun handleDeleteFile(file:StorageItem.File) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        requireContext().showAlertDialog(
            title = "Delete File",
            message = "Are you sure you want to delete this file?",
            positiveButtonText = "Delete"
        ).also {
            if(it) viewModel.deleteFile(file)
        }
    }
}
