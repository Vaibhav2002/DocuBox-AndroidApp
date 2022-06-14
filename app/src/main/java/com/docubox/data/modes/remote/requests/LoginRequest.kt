package com.docubox.data.modes.remote.requests

import com.google.gson.annotations.SerializedName

// API request to login a user
data class LoginRequest(
    @SerializedName("userEmail")
    val userEmail: String = "",
    @SerializedName("userPassword")
    val userPassword: String = ""
)
