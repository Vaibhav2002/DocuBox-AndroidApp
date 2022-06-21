package com.docubox.ui.screens.main.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.modes.local.SearchResult
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentHomeBinding
import com.docubox.util.extensions.launchAndCollect
import com.docubox.util.extensions.launchAndCollectLatest
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.extensions.showToast
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        initViews()
        initListeners()
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        binding.apply {
            tvStorageConsumption.text = "${it.storageUsed} / ${it.totalStorage} MB"
            storageProgress.max = it.totalStorage.toInt()
            storageProgress.progress = it.storageUsed.toInt()
        }
    }

    private fun collectUiEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            is HomeScreenEvents.NavigateToSearchResults -> navigateToSearchResults(
                it.title,
                it.files
            )
            is HomeScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }

    private fun initListeners() = with(binding) {
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH)
                viewModel.onSearch(searchBar.text.toString())
            true
        }
    }

    private fun initViews() = Unit

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Home")
    }

    private fun navigateToSearchResults(title: String, items: List<StorageItem>) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(
            SearchResult(items), title
        )
        findNavController().navigate(action)
    }

}
