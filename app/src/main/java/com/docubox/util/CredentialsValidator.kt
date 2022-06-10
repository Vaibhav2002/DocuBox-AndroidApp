package com.docubox.util

import android.text.TextUtils
import android.util.Patterns

private const val PASSWORD_LENGTH = 6

fun String.validateEmail(): String? = when {
    isEmpty() -> "Email cannot be empty"
    !isValidEmail() -> "Invalid email"
    else -> null
}

fun String.validatePassword(): String? = when {
    isEmpty() -> "Password cannot be empty"
    !isPasswordValid() -> "Minimum password length is $PASSWORD_LENGTH"
    else -> null
}

fun String.validateUsername() = when {
    isEmpty() -> "Username cannot be empty"
    else -> null
}

fun String.validateConfirmPassword(password: String) = when {
    password != this -> "Passwords do not match"
    else -> null
}

fun String.isValidEmail() =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordValid() = isNotEmpty() && length >= PASSWORD_LENGTH
