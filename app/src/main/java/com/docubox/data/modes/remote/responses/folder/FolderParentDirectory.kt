package com.docubox.data.modes.remote.responses.folder

import com.google.gson.annotations.SerializedName

data class FolderParentDirectory(
    @SerializedName("folderName")
    val folderName: String = "",
    @SerializedName("folderOwner")
    val folderOwner: String = "",
    @SerializedName("folderParentDirectory")
    val folderParentDirectory: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("__v")
    val v: Int = 0
)
