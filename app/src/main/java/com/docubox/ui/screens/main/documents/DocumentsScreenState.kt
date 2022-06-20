package com.docubox.ui.screens.main.documents

import com.docubox.data.modes.local.StorageItem

data class DocumentsScreenState(
    val storageItems: List<StorageItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing:Boolean = false,
    val actionBarTitle:String = "Documents"
)
