package com.docubox.data.modes.remote.responses.folder


import com.google.gson.annotations.SerializedName

data class CreateFolderResponse(
    @SerializedName("folder")
    val folder: FolderDto = FolderDto()
)