package com.docubox.ui.screens.main.shared

import com.docubox.data.modes.local.StorageItem

data class SharedScreenState(
    val storageItems:List<StorageItem> = emptyList(),
    val isLoading:Boolean = false,
    val isRefreshing:Boolean = false,
    val isSharedByMeState:Boolean = false
)
