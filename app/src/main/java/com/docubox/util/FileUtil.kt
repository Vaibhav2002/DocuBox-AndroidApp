package com.docubox.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

object FileUtil {
    fun getFileSize(context: Context, fileUri: Uri): Long {
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        return if (returnCursor != null) {
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val fileSize: Long = returnCursor.getLong(sizeIndex)
            returnCursor.close()
            fileSize
        } else {
            -1
        }
    }

    fun getMimeTypeFromFile(fileName: String): String? {
        val mimeMap = MimeTypeMap.getSingleton()
        val extension = MimeTypeMap.getFileExtensionFromUrl(fileName)
        return mimeMap.getMimeTypeFromExtension(extension)
    }

    fun getParsedFileUrl(url: String): String {
        return if (url.startsWith("ap-")) {
            "https://$url"
        } else {
            url
        }
    }

}