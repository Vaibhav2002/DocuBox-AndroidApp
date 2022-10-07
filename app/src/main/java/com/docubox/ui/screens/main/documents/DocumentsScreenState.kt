package com.docubox.ui.screens.main.documents

import com.docubox.data.local.models.StorageItem

data class DocumentsScreenState(
    val storageItems: List<StorageItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val actionBarTitle: String = "Documents",
    val storageUsed: Float = 0f,
    val totalStorage: Float = 50f,
)
