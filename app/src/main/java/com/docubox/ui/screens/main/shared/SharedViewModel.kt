package com.docubox.ui.screens.main.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.local.models.StorageItem
import com.docubox.data.remote.models.MessageResponse
import com.docubox.data.repo.StorageRepo
import com.docubox.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val storageRepo: StorageRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(SharedScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SharedScreenEvents>()
    val events = _events.asSharedFlow()

    private val isSharedByMeState = MutableStateFlow(false)

    init {
        collectIsSharedByMeState()
    }

    private fun collectIsSharedByMeState() = viewModelScope.launch {
        isSharedByMeState.collectLatest {
            _uiState.update { state -> state.copy(isSharedByMeState = it) }
            getSharedFiles(it)
        }
    }

    fun onRefresh() = viewModelScope.launch {
        getSharedFiles(uiState.value.isSharedByMeState, true)
    }

    private suspend fun getSharedFiles(
        isSharedByMe: Boolean,
        overrideProgressBar: Boolean = false
    ) {
        val reqFlow =
            if (isSharedByMe) storageRepo.getFilesSharedByMe() else storageRepo.getFilesSharedToMe()
        reqFlow.collectLatest {
            _uiState.emit(
                uiState.value.copy(
                    isLoading = it is Resource.Loading && !overrideProgressBar,
                    isRefreshing = it is Resource.Loading && overrideProgressBar
                )
            )
            when (it) {
                is Resource.Error -> _events.emit(SharedScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this::handleSharedFilesSuccess)
            }
        }
    }

    private fun handleSharedFilesSuccess(files: List<StorageItem.File>) {
        _uiState.update { it.copy(storageItems = files) }
    }

    fun onSharedByMeButtonPress() = viewModelScope.launch {
        isSharedByMeState.emit(true)
    }

    fun onSharedToMeButtonPress() = viewModelScope.launch {
        isSharedByMeState.emit(false)
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

    fun renameFile(file: StorageItem.File, newName: String) = viewModelScope.launch {
        storageRepo.renameFile(file, newName).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            handleMessageResponse(it)
        }
    }

    fun downloadFile(file: StorageItem.File) = viewModelScope.launch {
        storageRepo.downloadFile(file)
    }

    private suspend fun handleMessageResponse(
        res: Resource<MessageResponse>,
        getData: Boolean = true
    ) {
        if (res !is Resource.Loading) _events.emit(SharedScreenEvents.ShowToast(res.message))
        if (getData && res is Resource.Success) getSharedFiles(true)
    }
}