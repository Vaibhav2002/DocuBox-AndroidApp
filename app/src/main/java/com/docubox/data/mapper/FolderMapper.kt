package com.docubox.data.mapper

import com.docubox.data.local.models.StorageItem
import com.docubox.data.remote.models.responses.folder.FolderDto
import javax.inject.Inject

fun StorageItem.Folder.toRemote(): FolderDto = FolderDto(
    folderName = this.folder.folderName,
    folderOwner = this.folder.folderOwner,
//        folderParentDirectory = local.folder.folderParentDirectory,
    id = this.folder.id,
    v = this.folder.v,
)

fun FolderDto.toLocal(): StorageItem.Folder = StorageItem.Folder(this)

fun List<StorageItem.Folder>.toRemote(): List<FolderDto> =
    this.map { it.toRemote() }

fun List<FolderDto>.toLocal(): List<StorageItem.Folder> =
    this.map { it.toLocal() }