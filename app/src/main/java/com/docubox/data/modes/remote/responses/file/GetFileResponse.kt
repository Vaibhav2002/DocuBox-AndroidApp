package com.docubox.data.modes.remote.responses.file


import com.google.gson.annotations.SerializedName

data class GetFileResponse(
    @SerializedName("fileList")
    val fileList: List<FileDto> = listOf()
)