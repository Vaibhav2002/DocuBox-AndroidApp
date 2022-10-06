package com.docubox.data.remote.models

import com.google.gson.annotations.SerializedName

// User Data Transfer Object Class for representing structure of a User received from API
data class UserDto(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("userEmail")
    val userEmail: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("userDataFolder")
    val rootDirectory: List<String> = emptyList()
)
