package com.docubox.data.local.models

import androidx.annotation.DrawableRes
import com.docubox.R
import com.docubox.data.remote.models.responses.file.FileDto
import com.docubox.data.remote.models.responses.folder.FolderDto
import com.docubox.util.extensions.getFileType
import java.io.Serializable

// Class to represent a single storage item in app (Eg. a file or a folder)
sealed class StorageItem(
    open val id: String,
    open val name: String,
    open val description: String,
    @DrawableRes open val icon: Int
) : Serializable {
    data class File(
        val file: FileDto,
        val fileType: FileType = file.fileType.getFileType()
    ) : StorageItem(file.id, file.fileName, file.fileSize, fileType.icon)

    data class Folder(
        val folder: FolderDto
    ) : StorageItem(folder.id, folder.folderName, "", R.drawable.ic_folder)
}
