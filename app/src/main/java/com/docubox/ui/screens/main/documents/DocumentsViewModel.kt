package com.docubox.ui.screens.main.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.repo.StorageRepo
import com.docubox.util.Constants.DEFAULT_FILE_DIRECTORY
import com.docubox.util.Constants.DEFAULT_FOLDER_DIRECTORY
import com.docubox.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(private val storageRepo: StorageRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(DocumentsScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DocumentsScreenEvents>()
    val events = _events.asSharedFlow()

    init {
        //running both in parallel
        viewModelScope.launch { getFiles() }
        viewModelScope.launch { getFolders() }
    }

    private suspend fun getFiles() {
        storageRepo.getAllFiles(DEFAULT_FILE_DIRECTORY).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this::handleGetFileSuccess)
            }
        }
    }

    private suspend fun getFolders() {
        storageRepo.getAllFolders(DEFAULT_FOLDER_DIRECTORY).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this::handleGetFolderSuccess)
            }
        }
    }

    private fun handleGetFileSuccess(files: List<StorageItem.File>) {
        val newList = uiState.value.storageItems.toMutableList().apply { addAll(files) }
        _uiState.update { it.copy(storageItems = newList) }
    }

    private fun handleGetFolderSuccess(folders: List<StorageItem.Folder>) {
        val newList = uiState.value.storageItems.toMutableList().apply { addAll(folders) }
        _uiState.update { it.copy(storageItems = newList) }
    }
}
