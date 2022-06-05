package com.docubox.ui.screens.main.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.modes.local.StorageItem
import com.docubox.util.Constants.sampleStorageItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DocumentsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch { getFilesAndFolders() }
    }

    private suspend fun getFilesAndFolders() {
        // in real scenario if there are 2 lists one for folders 1 for files, then just sorting and adding is enough
        val folders = sampleStorageItems.filterIsInstance<StorageItem.Folder>().sortedBy { it.name }
        val files = sampleStorageItems.filterIsInstance<StorageItem.File>().sortedBy { it.name }
        _uiState.update { it.copy(storageItems = folders + files) }
    }
}
