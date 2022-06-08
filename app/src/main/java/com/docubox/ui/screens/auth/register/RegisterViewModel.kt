package com.docubox.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.util.validateConfirmPassword
import com.docubox.util.validateEmail
import com.docubox.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<RegisterScreenEvents>()
    val events = _events.asSharedFlow()

    fun onEmailTextChange(email: String) = viewModelScope.launch {
        _uiState.update { it.copy(email = email.trim()) }
    }

    fun onPasswordTextChange(password: String) = viewModelScope.launch {
        _uiState.update { it.copy(password = password.trim()) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) = viewModelScope.launch {
        _uiState.update { it.copy(confirmPassword = confirmPassword.trim()) }
    }

    private fun verifyUserInput(): Boolean {
        val emailError = uiState.value.email.validateEmail()
        val passwordError = uiState.value.password.validatePassword()
        val confirmPasswordError =
            uiState.value.confirmPassword.validateConfirmPassword(uiState.value.password)
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                confirmPwdError = confirmPasswordError
            )
        }
        return emailError == null && passwordError == null && confirmPasswordError == null
    }

    fun onRegisterButtonPressed() = viewModelScope.launch {
        if (verifyUserInput())
            registerUsingCredentials(
                uiState.value.email,
                uiState.value.password
            )
    }

    fun onGoToLoginPress() = viewModelScope.launch {
        _events.emit(RegisterScreenEvents.NavigateToLoginScreen)
    }

    private suspend fun registerUsingCredentials(
        email: String,
        password: String,
    ) {
//        authRepo.registerUser(email, password).collectLatest {
//            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
//            when (it) {
//                is Resource.Error -> _events.emit(RegisterScreenEvents.ShowToast(it.message))
//                is Resource.Loading -> Unit
//                is Resource.Success -> _events.emit(RegisterScreenEvents.NavigateToAvatarSelectScreen)
//            }
//        }
    }
}
