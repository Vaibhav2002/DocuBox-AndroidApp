package com.docubox.data.remote.models.responses

import com.google.gson.annotations.SerializedName

data class OwnerDto(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("userEmail")
    val userEmail: String = "",
    @SerializedName("userName")
    val userName: String = "",
    @SerializedName("__v")
    val v: Int = 0
)
