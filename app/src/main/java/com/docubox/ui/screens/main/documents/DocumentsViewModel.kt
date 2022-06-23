package com.docubox.ui.screens.main.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.local.dataSources.CacheData
import com.docubox.data.local.dataSources.StorageCache
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.remote.MessageResponse
import com.docubox.data.modes.remote.responses.StorageConsumption
import com.docubox.data.repo.PreferencesRepo
import com.docubox.data.repo.StorageRepo
import com.docubox.ui.screens.main.home.HomeScreenEvents
import com.docubox.util.Resource
import com.docubox.util.isValidEmail
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

    private var directory = preferencesRepo.getUser()?.rootDirectory

    init {
        getAllData(directory)
    }

    fun getCurrentDirectory() = directory

    val userToken = preferencesRepo.getUserToken()!!

    val storageLeft:Float
    get() = uiState.value.totalStorage - uiState.value.storageUsed

    fun getData() = viewModelScope.launch {
        getAllData(directory)
    }

    private fun getAllData(directory: String?, overrideProgressBar: Boolean = false) =
        viewModelScope.launch {
            _uiState.update { it.copy(storageItems = emptyList()) }
            listOf(
                async { getFiles(directory, overrideProgressBar) },
                async { getFolders(directory, overrideProgressBar) }
            ).awaitAll()
        }

    fun onRefresh() = viewModelScope.launch {
        getAllData(directory, true)
    }

    private suspend fun getFiles(directory: String?, overrideProgressBar: Boolean = false) {
        storageRepo.getAllFiles(directory).collectLatest {
            _uiState.emit(
                uiState.value.copy(
                    isLoading = it is Resource.Loading && !overrideProgressBar,
                    isRefreshing = it is Resource.Loading && overrideProgressBar
                )
            )
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this::handleGetFileSuccess)
            }
        }
    }


    private suspend fun getFolders(directory: String?, overrideProgressBar: Boolean = false) {
        storageRepo.getAllFolders(directory).collectLatest {
            _uiState.emit(
                uiState.value.copy(
                    isLoading = it is Resource.Loading && !overrideProgressBar,
                    isRefreshing = it is Resource.Loading && overrideProgressBar
                )
            )
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

    fun createFolder(folderName: String) = viewModelScope.launch {
        storageRepo.createFolder(folderName, directory ?: "").collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> getAllData(directory)
            }
        }
    }

    fun shareFile(file: StorageItem.File, email: String) = viewModelScope.launch {
        if (!email.isValidEmail()) {
            _events.emit(DocumentsScreenEvents.ShowToast("Invalid Email"))
            return@launch
        }
        storageRepo.shareFile(file.file.id, email).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun revokeShareFile(file: StorageItem.File, email: String) = viewModelScope.launch {
        storageRepo.revokeShareFile(file.file.id, email).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun deleteFile(file: StorageItem.File) = viewModelScope.launch {
        storageRepo.deleteFile(file.file.id).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun downloadFile(file: StorageItem.File) = viewModelScope.launch {
        storageRepo.downloadFile(file)
    }

    fun deleteFolder(folder: StorageItem.Folder) = viewModelScope.launch {
        storageRepo.deleteFolder(folder.folder.id).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun renameFile(file:StorageItem.File, newName:String) = viewModelScope.launch {
        storageRepo.renameFile(file, newName).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun renameFolder(folder:StorageItem.Folder, newName:String) = viewModelScope.launch {
        storageRepo.renameFolder(folder, newName).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    private suspend fun handleMessageResponse(
        res: Resource<MessageResponse>,
        getData: Boolean = true
    ) {
        if (res !is Resource.Loading) _events.emit(DocumentsScreenEvents.ShowToast(res.message))
        if (getData && res is Resource.Success) getAllData(directory)
    }


    fun getStorageConsumption() = viewModelScope.launch {
        storageRepo.getStorageConsumption().collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(DocumentsScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this@DocumentsViewModel::handleStorageConsumptionSuccess)
            }
        }
    }

    private fun handleStorageConsumptionSuccess(storageConsumption: StorageConsumption) {
        _uiState.update { state ->
            state.copy(
                storageUsed = storageConsumption.storageConsumption.toFloatOrNull() ?: 0f,
                totalStorage = storageConsumption.totalStorage.toFloatOrNull() ?: 0f
            )
        }
    }
}


