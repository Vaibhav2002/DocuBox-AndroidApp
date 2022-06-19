package com.docubox.data.modes.remote.requests


import com.google.gson.annotations.SerializedName

data class CreateFolderRequest(
    @SerializedName("folderName")
    val folderName: String = "",
    @SerializedName("folderParentDirectory")
    val folderParentDirectory: String = ""
)