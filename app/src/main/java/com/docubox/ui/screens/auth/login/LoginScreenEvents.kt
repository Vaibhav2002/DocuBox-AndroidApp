package com.docubox.ui.screens.auth.login

sealed class LoginScreenEvents {
    data class ShowToast(val message: String) : LoginScreenEvents()
    object NavigateToMainScreen : LoginScreenEvents()
    object NavigateToRegisterScreen : LoginScreenEvents()
}
