package com.docubox.data.remote.models.requests


import com.google.gson.annotations.SerializedName

data class GetFolderRequest(
    @SerializedName("folderParentDirectory")
    val folderParentDirectory: String? = ""
)