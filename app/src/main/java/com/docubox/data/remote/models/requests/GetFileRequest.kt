package com.docubox.data.remote.models.requests


import com.google.gson.annotations.SerializedName

data class GetFileRequest(
    @SerializedName("fileDirectory")
    val fileDirectory: String? = ""
)