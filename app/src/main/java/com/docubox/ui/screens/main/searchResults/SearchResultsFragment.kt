package com.docubox.ui.screens.main.searchResults

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.docubox.R
import com.docubox.data.modes.local.FileOption
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentSearchResultsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.ui.adapter.OneAdapter
import com.docubox.ui.screens.main.documents.DocumentsFragmentDirections
import com.docubox.ui.screens.main.shared.SharedFragmentDirections
import com.docubox.util.Constants
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private val binding by viewBinding(FragmentSearchResultsBinding::bind)
    private val viewModel by viewModels<SearchResultsViewModel>()
    private val args by navArgs<SearchResultsFragmentArgs>()
    private lateinit var resultsAdapter: OneAdapter<StorageItem.File, ItemStorageBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSearchResults(args.results.results)
        initViews()
        collectUiEvents()
        collectUiState()
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        resultsAdapter.submitList(it.items)
        binding.progressLayout.visibleOrGone(it.isLoading)
    }

    private fun collectUiEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            SearchResultsScreenEvents.NavigateBack -> findNavController().popBackStack()
            is SearchResultsScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }


    private fun initViews() = with(binding) {
        storageRv.setHasFixedSize(false)
        resultsAdapter = storageRv.compose(
            ItemStorageBinding::inflate,
            onBind = { item: StorageItem.File, _ ->
                title.text = item.name
                description.text = item.description
                itemImage.setImageResource(item.icon)
                root.setOnLongClickListener {
                    onFileLongPressed(item)
                    true
                }
            }
        ) {
            handleStorageItemPress(this)
        }
        actionBar.setupActionBar(args.title, true, {
            findNavController().popBackStack()
        })
    }

    private fun handleStorageItemPress(item: StorageItem) {
        when (item) {
            is StorageItem.Folder -> Unit
            is StorageItem.File -> {
                val action = SearchResultsFragmentDirections.actionSearchResultsFragmentToViewDocumentFragment(item)
                findNavController().navigate(action)
            }
        }
    }

    private fun onFileLongPressed(file: StorageItem.File) {
        val options = Constants.fileOptions.toMutableList().apply {
            if (file.file.fileSharedTo.isEmpty()) remove(FileOption.RevokeShare)
        }
        showFileOptions(
            file = file,
            options = options,
            onDelete = viewModel::deleteFile,
            onShare = viewModel::shareFile,
            onRevokeShare = viewModel::revokeShareFile,
            onDownload = viewModel::downloadFile,
            onRename = viewModel::renameFile
        )
    }

}