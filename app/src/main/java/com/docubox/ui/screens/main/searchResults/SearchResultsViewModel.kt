package com.docubox.ui.screens.main.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.modes.local.StorageItem
import com.docubox.data.repo.StorageRepo
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

    fun setSearchResults(results:List<StorageItem>) = viewModelScope.launch {
        _uiState.update { it.copy(items = results) }
    }
}