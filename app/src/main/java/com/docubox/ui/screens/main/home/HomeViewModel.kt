package com.docubox.ui.screens.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.modes.remote.responses.StorageConsumption
import com.docubox.data.repo.StorageRepo
import com.docubox.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storageRepo: StorageRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<HomeScreenEvents>()
    val events = _events.asSharedFlow()

    init {
        getStorageConsumption()
    }

    private fun getStorageConsumption() = viewModelScope.launch {
        storageRepo.getStorageConsumption().collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(HomeScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> it.data?.let(this@HomeViewModel::handleStorageConsumptionSuccess)
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