package com.docubox.data.remote.models.responses.folder


import com.google.gson.annotations.SerializedName

data class CreateFolderResponse(
    @SerializedName("folder")
    val folder: FolderDto = FolderDto()
)