package com.docubox.data.modes.remote.responses.folder

import com.docubox.data.modes.remote.responses.ParentDirectory
import com.docubox.data.modes.remote.responses.OwnerDto
import com.google.gson.annotations.SerializedName

data class FolderDto(
    @SerializedName("folderName")
    val folderName: String = "",
    @SerializedName("folderOwner")
    val folderOwner: OwnerDto = OwnerDto(),
    @SerializedName("folderParentDirectory")
    val folderParentDirectory: ParentDirectory = ParentDirectory(),
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("__v")
    val v: Int = 0
)
