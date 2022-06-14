package com.docubox.data.modes.remote

import com.google.gson.annotations.SerializedName

data class ErrorDto(
    @SerializedName("message")
    val message: String = ""
)
