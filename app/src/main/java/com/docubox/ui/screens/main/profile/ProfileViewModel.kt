package com.docubox.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.domain.repo.AuthRepo
import com.docubox.domain.repo.PreferenceRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceRepo: PreferenceRepo,
    private val authRepo: AuthRepo, // Repository that contains data store functions
) : ViewModel() {
    // Function to get a user from datastore using Preference Repository
    fun getUser() = preferenceRepo.getUser()

    fun logoutUser() = viewModelScope.launch {
        authRepo.logoutUser()
    }

}