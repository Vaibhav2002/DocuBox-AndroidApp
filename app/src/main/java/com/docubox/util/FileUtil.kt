package com.docubox.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns

object FileUtil {
    fun getFileSize(context: Context,fileUri: Uri): Long {
        val returnCursor = context.contentResolver.query(fileUri, null, null, null, null)
        return if(returnCursor!=null) {
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val fileSize: Long = returnCursor.getLong(sizeIndex)
            returnCursor.close()
            fileSize
        } else {
            -1
        }
    }
}