package com.docubox.data.modes.remote.responses.file

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @SerializedName("file")
    val `file`: UploadedFile = UploadedFile()
)