package com.docubox.ui.screens.main.shared

import com.docubox.data.local.models.StorageItem

data class SharedScreenState(
    val storageItems: List<StorageItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSharedByMeState: Boolean = false
)
