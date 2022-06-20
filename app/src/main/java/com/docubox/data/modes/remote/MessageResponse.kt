package com.docubox.data.modes.remote

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String = ""
)
