package com.docubox.data.remote.models.requests

import com.google.gson.annotations.SerializedName

// API request to signup a user
data class RegisterRequest(
    @SerializedName("userEmail")
    val userEmail: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userPassword")
    val userPassword: String = ""
)
