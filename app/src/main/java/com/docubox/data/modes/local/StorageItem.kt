package com.docubox.data.modes.local

import androidx.annotation.DrawableRes
import com.docubox.R

sealed class StorageItem(
    open val id: String,
    open val name: String,
    open val description: String,
    @DrawableRes open val icon: Int
) {
    data class File(
        override val id: String,
        override val name: String,
        override val description: String,
        val fileType: FileType
    ) : StorageItem(id, name, description, fileType.icon)

    data class Folder(
        override val id: String,
        override val name: String,
        override val description: String,
    ) : StorageItem(id, name, description, R.drawable.ic_folder)
}
