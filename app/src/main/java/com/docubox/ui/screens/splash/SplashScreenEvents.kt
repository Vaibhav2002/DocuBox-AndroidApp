package com.docubox.ui.screens.splash

sealed class SplashScreenEvents {
    object NavigateToAuthScreen : SplashScreenEvents()
    object NavigateToHomeScreen : SplashScreenEvents()
}
