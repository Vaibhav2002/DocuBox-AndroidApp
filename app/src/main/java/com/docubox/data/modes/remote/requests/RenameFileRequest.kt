package com.docubox.data.modes.remote.requests

import com.google.gson.annotations.SerializedName

data class RenameFileRequest(
    @SerializedName("fileId")
    val fileId: String,
    @SerializedName("newName")
    val newFileName: String,
)
