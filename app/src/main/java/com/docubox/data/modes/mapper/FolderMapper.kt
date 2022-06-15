package com.docubox.data.modes.mapper

import com.docubox.data.modes.local.StorageItem
import com.docubox.data.modes.remote.responses.folder.FolderDto
import javax.inject.Inject

class FolderMapper @Inject constructor() : Mapper<StorageItem.Folder, FolderDto> {
    override fun toRemote(local: StorageItem.Folder): FolderDto = FolderDto(
        folderName = local.folder.folderName,
        folderOwner = local.folder.folderOwner,
        folderParentDirectory = local.folder.folderParentDirectory,
        id = local.folder.id,
        v = local.folder.v,
    )

    override fun toLocal(remote: FolderDto): StorageItem.Folder = StorageItem.Folder(remote)

    override fun toRemote(local: List<StorageItem.Folder>): List<FolderDto> =
        local.map { toRemote(it) }

    override fun toLocal(remote: List<FolderDto>): List<StorageItem.Folder> =
        remote.map { toLocal(it) }
}