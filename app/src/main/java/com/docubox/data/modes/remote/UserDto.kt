package com.docubox.data.modes.remote

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("token")
    val token: String = "",
    @SerializedName("userEmail")
    val userEmail: String = "",
    @SerializedName("userName")
    val userName: String = ""
)