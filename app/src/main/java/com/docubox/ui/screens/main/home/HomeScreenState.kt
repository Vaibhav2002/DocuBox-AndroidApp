package com.docubox.ui.screens.main.home

data class HomeScreenState(
    val storageUsed: Float = 0f,
    val totalStorage: Float = 50f,
    val isLoading: Boolean = false
)
