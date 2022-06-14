package com.docubox.ui.screens.main.profile

import androidx.lifecycle.ViewModel
import com.docubox.data.modes.local.User
import com.docubox.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepo: PreferencesRepo, // Repository that contains data store functions
): ViewModel() {
    // Function to get a user from datastore using Preference Repository
    fun getUser(): User? {
        return preferencesRepo.getUser()
    }

    fun logoutUser() {
        return preferencesRepo.removeUser()
    }

}