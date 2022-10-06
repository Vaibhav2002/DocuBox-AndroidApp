package com.docubox.data.remote.models.responses.file

import com.docubox.data.remote.models.responses.OwnerDto
import com.docubox.data.remote.models.responses.ParentDirectory
import com.google.gson.annotations.SerializedName

data class UploadedFile(
    @SerializedName("fileDirectory")
    val fileDirectory: ParentDirectory = ParentDirectory(),
    @SerializedName("fileName")
    val fileName: String = "",
    @SerializedName("fileOwner")
    val fileOwner: OwnerDto = OwnerDto(),
    @SerializedName("fileSize")
    val fileSize: String = "",
    @SerializedName("fileStorageUrl")
    val fileStorageUrl: String = "",
    @SerializedName("fileType")
    val fileType: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("__v")
    val v: Int = 0
)