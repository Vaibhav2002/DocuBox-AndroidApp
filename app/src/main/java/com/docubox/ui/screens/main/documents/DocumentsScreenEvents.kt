package com.docubox.ui.screens.main.documents

sealed class DocumentsScreenEvents {
    data class ShowToast(val message:String):DocumentsScreenEvents()
    object NavigateBack:DocumentsScreenEvents()
}