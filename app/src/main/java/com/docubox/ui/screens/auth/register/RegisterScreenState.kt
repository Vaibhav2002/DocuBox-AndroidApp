package com.docubox.ui.screens.auth.register

data class RegisterScreenState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,

    val confirmPwdError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isRegisterButtonEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && !isLoading

    val areTextFieldsEnabled: Boolean
        get() = !isLoading
}
