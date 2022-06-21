package com.docubox.ui.screens.main.searchResults

import com.docubox.data.modes.local.StorageItem

data class SearchResultsScreenState(
    val items: List<StorageItem.File> = emptyList(),
    val isLoading: Boolean = false
)