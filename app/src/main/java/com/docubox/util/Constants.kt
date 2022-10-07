package com.docubox.util

import android.Manifest
import android.os.Build
import com.docubox.data.local.models.FileOption
import com.docubox.data.local.models.FolderOptions

// All our app's constant data variables
object Constants {

    const val DATASTORE_NAME = "DocuBoxDataStore"

    const val FILE_OPTION_DIALOG = "FileOptionsDialog"
    const val FOLDER_OPTION_DIALOG = "FolderOptionsDialog"

    const val REPORT_BUG_URL = "https://github.com/ishantchauhan710/DocuBox-AndroidApp/issues"
    const val VIEW_SOURCE_CODE_URL = "https://github.com/ishantchauhan710/DocuBox-AndroidApp"

    const val HOW_TO_USE_URL = "https://github.com/Vaibhav2002/DocuBox-AndroidApp/blob/master/HOW%20TO%20USE.md"
    const val CONTACT_US_URL = "https://github.com/Vaibhav2002/DocuBox-AndroidApp/discussions"

    val folderOptions = listOf(
        FolderOptions.Rename,
        FolderOptions.Delete
    )

    val fileOptions = listOf(
        FileOption.Rename,
        FileOption.Download,
        FileOption.Share,
        FileOption.RevokeShare,
        FileOption.Delete,
    )

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
