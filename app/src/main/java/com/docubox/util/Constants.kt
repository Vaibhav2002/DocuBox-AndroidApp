package com.docubox.util

import android.Manifest
import android.os.Build
import com.docubox.data.modes.local.FileType
import com.docubox.data.modes.local.StorageItem

// All our app's constant data variables
object Constants {

    const val DATASTORE_NAME = "DocuBoxDataStore"
    val DEFAULT_FILE_DIRECTORY: String? = null
    val DEFAULT_FOLDER_DIRECTORY: String? = null

    val filePermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            toMutableList().add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    }

//    val sampleStorageItems = listOf(
//        StorageItem.File(
//            id = "1",
//            name = "Ishant.pdf",
//            description = "120KB",
//            fileType = FileType.Document
//        ),
//        StorageItem.File(
//            id = "2",
//            name = "Vaibhav.exe",
//            description = "1.2MB",
//            fileType = FileType.File
//        ),
//        StorageItem.File(
//            id = "3",
//            name = "GoogleIO.mp4",
//            description = "120MB",
//            fileType = FileType.Video
//        ),
//        StorageItem.Folder(id = "4", name = "College", description = "10 files"),
//        StorageItem.Folder(id = "5", name = "Downloads", description = "2000 files"),
//    ).shuffled()
}
