package com.docubox.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.repo.AuthRepo
import com.docubox.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepo: PreferencesRepo,
    private val authRepo: AuthRepo, // Repository that contains data store functions
) : ViewModel() {
    // Function to get a user from datastore using Preference Repository
    fun getUser() = preferencesRepo.getUser()

    fun logoutUser() = viewModelScope.launch {
        authRepo.logoutUser()
    }

}