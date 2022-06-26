package com.docubox.data.modes.local

import androidx.annotation.DrawableRes

sealed class FileOption(val text: String, @DrawableRes val icon: Int = 0) {
    object Download : FileOption("Download")
    object Rename : FileOption("Rename")
    object Share : FileOption("Share Access")
    object RevokeShare : FileOption("Revoke Share Access")
    object Delete : FileOption("Delete File")
}
