package com.docubox.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _events = MutableSharedFlow<SplashScreenEvents>()
    val events = _events.asSharedFlow()

    init {
        navigate()
    }

    private fun navigate() = viewModelScope.launch {
        delay(2000L)
        _events.emit(getNavigationEvent())
    }

    private fun getNavigationEvent() = when {
        authRepo.isUserLoggedIn() -> SplashScreenEvents.NavigateToHomeScreen
        else -> SplashScreenEvents.NavigateToAuthScreen
    }
}
