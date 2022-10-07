package com.docubox.data.mapper

import com.docubox.data.local.models.StorageItem
import com.docubox.data.remote.models.responses.file.FileDto
import javax.inject.Inject

fun StorageItem.File.toRemote() = FileDto(
    fileDirectory = this.file.fileDirectory,
    fileName = this.file.fileName,
//        fileOwner = local.file.fileOwner,
    fileType = this.file.fileType,
    fileSize = this.file.fileSize,
    fileStorageUrl = this.file.fileStorageUrl,
    id = this.file.id,
    v = this.file.v,
)

fun FileDto.toLocal(): StorageItem.File = StorageItem.File(
    file = this
)

fun List<StorageItem.File>.toRemote(): List<FileDto> = this.map {
    it.toRemote()
}

fun List<FileDto>.toLocal(): List<StorageItem.File> = this.map {
    it.toLocal()
}