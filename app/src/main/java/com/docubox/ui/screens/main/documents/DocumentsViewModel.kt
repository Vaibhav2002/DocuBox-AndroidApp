package com.docubox.ui.screens.main.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.local.dataSources.CacheData
import com.docubox.data.local.dataSources.StorageCache
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.repo.PreferencesRepo
import com.docubox.data.repo.StorageRepo
import com.docubox.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DocumentsViewModel @Inject constructor(
    private val storageRepo: StorageRepo,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    companion object {
        const val ROOT_FOLDER_NAME = "Documents"
    }

    private val _uiState = MutableStateFlow(DocumentsScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DocumentsScreenEvents>()
    val events = _events.asSharedFlow()

    private var directory: String? = null

    init {
        getAllData(directory)
    }

    fun getCurrentDirectory() = directory

    val userToken = preferencesRepo.getUserToken()!!

    private fun getAllData(directory: String?) = viewModelScope.launch {
        _uiState.update { it.copy(storageItems = emptyList()) }
        listOf(async { getFiles(directory) }, async { getFolders(directory) }).awaitAll()
    }

    private suspend fun getFiles(directory: String?) {
        storageRepo.getAllFiles(directory).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this::handleGetFileSuccess)
            }
        }
    }


    private suspend fun getFolders(directory: String?) {
        storageRepo.getAllFolders(directory).collectLatest {
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

    private fun replaceData(data: List<StorageItem>) {
        _uiState.update { it.copy(storageItems = data) }
    }

    private fun updateActionBarTitle(folderName: String?) {
        _uiState.update { it.copy(actionBarTitle = folderName ?: ROOT_FOLDER_NAME) }
    }

    fun onBackPress() = viewModelScope.launch {
        if (StorageCache.isEmpty())
            _events.emit(DocumentsScreenEvents.NavigateBack)
        else
            StorageCache.popCache().apply {
                replaceData(items)
                this@DocumentsViewModel.directory = directory
                updateActionBarTitle(folderName)
            }
    }

    fun onFolderPress(folder: StorageItem.Folder) = viewModelScope.launch {
        StorageCache.addToCache(
            CacheData(
                uiState.value.storageItems,
                directory,
                uiState.value.actionBarTitle
            )
        )
        directory = folder.folder.id
        getAllData(directory)
        updateActionBarTitle(folder.folder.folderName)
    }

    fun createFolder(folderName:String) = viewModelScope.launch {
        storageRepo.createFolder(folderName, directory?:"").collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this@DocumentsViewModel::createFolderSuccess)
            }
        }
    }

    private fun createFolderSuccess(folder:StorageItem.Folder){
        val newItems = uiState.value.storageItems.toMutableList().apply { add(folder) }
        _uiState.update { it.copy(storageItems = newItems) }
    }
}
