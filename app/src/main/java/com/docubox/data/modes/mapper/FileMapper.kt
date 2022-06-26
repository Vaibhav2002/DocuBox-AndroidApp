package com.docubox.data.modes.mapper

import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.remote.responses.file.FileDto
import javax.inject.Inject

class FileMapper @Inject constructor() : Mapper<StorageItem.File, FileDto> {
    override fun toRemote(local: StorageItem.File): FileDto = FileDto(
        fileDirectory = local.file.fileDirectory,
        fileName = local.file.fileName,
//        fileOwner = local.file.fileOwner,
        fileType = local.file.fileType,
        fileSize = local.file.fileSize,
        fileStorageUrl = local.file.fileStorageUrl,
        id = local.file.id,
        v = local.file.v,
    )

    override fun toLocal(remote: FileDto): StorageItem.File = StorageItem.File(
        file = remote
    )

    override fun toRemote(local: List<StorageItem.File>): List<FileDto> = local.map { toRemote(it) }

    override fun toLocal(remote: List<FileDto>): List<StorageItem.File> = remote.map { toLocal(it) }
}