package com.docubox.data.modes.remote.requests


import com.google.gson.annotations.SerializedName

data class GetFolderRequest(
    @SerializedName("folderParentDirectory")
    val folderParentDirectory: String? = ""
)