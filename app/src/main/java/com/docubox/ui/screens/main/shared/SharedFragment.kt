package com.docubox.ui.screens.main.shared

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.modes.local.FileOption
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentSharedBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.ui.adapter.OneAdapter
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
                    when (item) {
                        is StorageItem.File -> handleFileLongPress(item)
                        is StorageItem.Folder -> Unit
                    }
                    true
                }
            },
        ) {
            handleStorageItemPress(this)
        }
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Shared", true, {
            findNavController().popBackStack()
        })
    }

    private fun handleStorageItemPress(item: StorageItem) {
        when (item) {
            is StorageItem.Folder -> Unit
            is StorageItem.File -> {
                val action =
                    SharedFragmentDirections.actionSharedFragmentToViewDocumentFragment(item.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun handleFileLongPress(file: StorageItem.File) {
        val options = if (viewModel.uiState.value.isSharedByMeState)
            listOf(FileOption.RevokeShare, FileOption.Download, FileOption.Delete)
        else listOf(FileOption.Download)
        showFileOptions(
            file = file,
            options = options,
            onRevokeShare = viewModel::revokeShareFile,
            onDelete = viewModel::deleteFile,
            onDownload = viewModel::downloadFile
        )
    }
}
