package com.docubox.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docubox.data.repo.AuthRepo
import com.docubox.util.Resource
import com.docubox.util.validateEmail
import com.docubox.util.validatePassword
import com.docubox.util.validateUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

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

    fun onUsernameChange(username: String) = viewModelScope.launch {
        _uiState.update { it.copy(username = username.trim()) }
    }

    private fun verifyUserInput(): Boolean {
        val emailError = uiState.value.email.validateEmail()
        val passwordError = uiState.value.password.validatePassword()
        val usernameError = uiState.value.username.validateUsername()
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                usernameError = usernameError
            )
        }
        return listOf(emailError, passwordError, usernameError).all { it == null }
    }

    fun onRegisterButtonPressed() = viewModelScope.launch {
        if (verifyUserInput())
            registerUsingCredentials(
                uiState.value.username,
                uiState.value.email,
                uiState.value.password
            )
    }

    fun onGoToLoginPress() = viewModelScope.launch {
        _events.emit(RegisterScreenEvents.NavigateToLoginScreen)
    }

    private suspend fun registerUsingCredentials(
        username: String,
        email: String,
        password: String,
    ) {

        authRepo.registerUser(username, email, password).collectLatest {
            _uiState.emit(uiState.value.copy(isLoading = it is Resource.Loading))
            when (it) {
                is Resource.Error -> _events.emit(RegisterScreenEvents.ShowToast(it.message))
                is Resource.Loading -> Unit
                is Resource.Success -> _events.emit(RegisterScreenEvents.NavigateToHomeScreen)
            }
        }
    }
}
