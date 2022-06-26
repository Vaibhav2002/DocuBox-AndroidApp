package com.docubox.ui.screens.main.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.repo.StorageRepo
import com.docubox.util.Resource
import com.docubox.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(private val storageRepo: StorageRepo) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SearchResultsScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SearchResultsScreenEvents>()
    val events = _events.asSharedFlow()

    fun setSearchResults(results: List<StorageItem.File>) = viewModelScope.launch {
        _uiState.update { it.copy(items = results) }
    }

    fun shareFile(file: StorageItem.File, email: String) = viewModelScope.launch {
        if (!email.isValidEmail()) {
            _events.emit(SearchResultsScreenEvents.ShowToast("Invalid Email"))
            return@launch
        }
        storageRepo.shareFile(file.file.id, email).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            if (it is Resource.Success) handleShareFileSuccess(file, email)
        }
    }

    fun revokeShareFile(file: StorageItem.File, email: String) = viewModelScope.launch {
        storageRepo.revokeShareFile(file.file.id, email).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            if (it is Resource.Success) handleRevokeFileSuccess(file, email)
        }
    }

    fun deleteFile(file: StorageItem.File) = viewModelScope.launch {
        storageRepo.deleteFile(file.file.id).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            if (it is Resource.Success) handleDeleteFileSuccess(file)
        }
    }

    fun renameFile(file: StorageItem.File, newName: String) = viewModelScope.launch {
        storageRepo.renameFile(file, newName).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            val newFileName = "${newName}.${file.name.substringAfterLast('.')}"
            replaceFile(file) { file ->
                val newFile = file.file.copy(fileName = newFileName)
                file.copy(file = newFile)
            }
        }
    }

    fun downloadFile(file: StorageItem.File) = viewModelScope.launch {
        storageRepo.downloadFile(file)
    }

    private fun handleShareFileSuccess(sharedFile: StorageItem.File, email: String) {
        replaceFile(sharedFile) {
            val newShares = it.file.fileSharedTo.toMutableList().apply { add(email) }
            it.copy(file = it.file.copy(fileSharedTo = newShares))
        }
    }

    private fun handleRevokeFileSuccess(sharedFile: StorageItem.File, email: String) {
        replaceFile(sharedFile) {
            val newShares = it.file.fileSharedTo.toMutableList().apply { remove(email) }
            it.copy(file = it.file.copy(fileSharedTo = newShares))
        }
    }

    private fun handleDeleteFileSuccess(file: StorageItem.File) {
        val newItems = uiState.value.items.toMutableList().apply { remove(file) }
        _uiState.update { it.copy(items = newItems) }
    }

    private fun replaceFile(
        file: StorageItem.File,
        newFile: (StorageItem.File) -> StorageItem.File
    ) {
        val fileIndex = uiState.value.items.indexOfFirst { file.id == it.id }
        val newList = uiState.value.items.toMutableList().apply {
            set(fileIndex, newFile(file))
        }
        _uiState.update { it.copy(items = newList) }
    }
}