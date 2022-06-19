package com.docubox.data.modes.remote.responses.file


import com.docubox.data.modes.remote.responses.OwnerDto
import com.google.gson.annotations.SerializedName

data class FileDto(
    @SerializedName("fileDirectory")
    val fileDirectory: String = "",
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
    val v: Int = 0,
    @SerializedName("fileSharedTo")
    val fileSharedTo: List<String> = emptyList()

)