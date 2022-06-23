package com.docubox.data.modes.remote.requests

import com.google.gson.annotations.SerializedName

data class RenameFolderRequest(
    @SerializedName("folderId")
    val folderId: String,
    @SerializedName("newName")
    val newFileName: String,
)