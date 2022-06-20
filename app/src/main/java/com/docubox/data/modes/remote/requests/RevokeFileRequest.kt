package com.docubox.data.modes.remote.requests

import com.google.gson.annotations.SerializedName

data class RevokeFileRequest(
    @SerializedName("fileId")
    val fileId: String,
    @SerializedName("userToRevokeEmail")
    val email: String
)