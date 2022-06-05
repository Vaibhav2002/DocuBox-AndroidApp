package com.docubox.util

import android.Manifest
import android.os.Build

object Constants {

    val filePermissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            toMutableList().add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
    }
}
