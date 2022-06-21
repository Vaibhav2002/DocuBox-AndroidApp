package com.docubox.ui.screens.main.searchResults

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.docubox.R
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentSearchResultsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.ui.adapter.OneAdapter
import com.docubox.util.extensions.*
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : Fragment(R.layout.fragment_search_results) {

    private val binding by viewBinding(FragmentSearchResultsBinding::bind)
    private val viewModel by viewModels<SearchResultsViewModel>()
    private val args by navArgs<SearchResultsFragmentArgs>()
    private lateinit var resultsAdapter: OneAdapter<StorageItem, ItemStorageBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectUiEvents()
        collectUiState()
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        resultsAdapter.submitList(it.items)
    }

    private fun collectUiEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            SearchResultsScreenEvents.NavigateBack -> findNavController().popBackStack()
            is SearchResultsScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }


    private fun initViews() = with(binding) {
        storageRv.setHasFixedSize(false)
        storageRv.compose(
            ItemStorageBinding::inflate,
            onBind = { item: StorageItem, _ ->
                title.text = item.name
                description.text = item.description
                itemImage.setImageResource(item.icon)
            }
        ){

        }
        actionBar.setupActionBar(args.title, true, {
            findNavController().popBackStack()
        })
    }


}