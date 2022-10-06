package com.docubox.data.remote.models.requests

import com.google.gson.annotations.SerializedName

data class ShareFileRequest(
    @SerializedName("fileId")
    val fileId: String,
    @SerializedName("userToShareEmail")
    val email: String
)