package com.docubox.data.modes.remote.requests


import com.google.gson.annotations.SerializedName

data class GetFileRequest(
    @SerializedName("fileDirectory")
    val fileDirectory: String? = ""
)