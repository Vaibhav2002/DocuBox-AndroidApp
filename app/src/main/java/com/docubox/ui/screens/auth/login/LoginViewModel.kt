package com.docubox.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.util.validateEmail
import com.docubox.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginScreenEvents>()
    val events = _events.asSharedFlow()

    fun onEmailTextChange(email: String) = viewModelScope.launch {
        _uiState.update { it.copy(email = email.trim()) }
    }

    fun onPasswordTextChange(password: String) = viewModelScope.launch {
        _uiState.update { it.copy(password = password.trim()) }
    }

    private fun verifyUserInput(): Boolean {
        val emailError = uiState.value.email.validateEmail()
        val passwordError = uiState.value.password.validatePassword()
        _uiState.update {
            it.copy(emailError = emailError, passwordError = passwordError)
        }
        return emailError == null && passwordError == null
    }

    fun onLoginButtonPressed() = viewModelScope.launch {
        if (verifyUserInput())
            loginUsingCredentials(uiState.value.email, uiState.value.password)
    }

    fun onGoToRegisterPress() = viewModelScope.launch {
        _events.emit(LoginScreenEvents.NavigateToRegisterScreen)
    }

    private suspend fun loginUsingCredentials(email: String, password: String) {
//        authRepo.loginUser(email, password).collectLatest {
//            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
//            when (it) {
//                is Resource.Error -> _events.emit(LoginScreenEvents.ShowToast(it.message))
//                is Resource.Loading -> Unit
//                is Resource.Success -> _events.emit(LoginScreenEvents.NavigateToMainScreen)
//            }
//        }
    }
}
