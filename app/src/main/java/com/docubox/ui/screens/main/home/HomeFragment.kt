package com.docubox.ui.screens.main.home

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.local.models.FileType
import com.docubox.data.local.models.SearchResult
import com.docubox.data.local.models.StorageItem
import com.docubox.databinding.FragmentHomeBinding
import com.docubox.util.Constants.CONTACT_US_URL
import com.docubox.util.Constants.HOW_TO_USE_URL
import com.docubox.util.extensions.*
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
            storageProgress.progress = it.storageUsed.toInt() % it.totalStorage.toInt()
            progressLayout.visibleOrGone(it.isLoading)
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
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearch(searchBar.text.toString())
                searchBar.hideKeyboard()
            }
            true
        }
        imageOption.singleClick { viewModel.onFileTypePress(FileType.Image) }
        videoOption.singleClick { viewModel.onFileTypePress(FileType.Video) }
        audioOption.singleClick { viewModel.onFileTypePress(FileType.Audio) }
        docsOption.singleClick { viewModel.onFileTypePress(FileType.File) }
        downloads.singleClick(this@HomeFragment::openDownloadsFolder)
        howToUse.singleClick { requireContext().openBrowser(HOW_TO_USE_URL) }
        aboutUs.singleClick {
            findNavController().navigate(R.id.action_homeFragment_to_aboutUsBottomSheetFragment)
        }
        contactUs.singleClick { requireContext().openBrowser(CONTACT_US_URL) }
        btnRefreshStorageConsumption.singleClick(viewModel::getStorageConsumption)
    }

    private fun initViews() = Unit

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Home")
    }

    private fun navigateToSearchResults(title: String, items: List<StorageItem.File>) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(
            SearchResult(items), title
        )
        findNavController().navigate(action)
    }

    private fun openDownloadsFolder() {
        Intent(DownloadManager.ACTION_VIEW_DOWNLOADS).also {
            startActivity(it)
        }
    }


}
