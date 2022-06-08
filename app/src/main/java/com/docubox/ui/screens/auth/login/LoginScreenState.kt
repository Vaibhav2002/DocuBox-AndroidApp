package com.docubox.ui.screens.auth.login

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,

    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isLoginButtonEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading

    val areTextFieldsEnabled: Boolean
        get() = !isLoading
}
