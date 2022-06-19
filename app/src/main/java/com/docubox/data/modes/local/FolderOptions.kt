package com.docubox.data.modes.local

import androidx.annotation.DrawableRes

sealed class FolderOptions(val text: String, @DrawableRes val icon: Int = 0) {
    object Rename : FolderOptions("Rename")
    object Share : FolderOptions("Share Access")
    object RevokeShare : FolderOptions("Revoke Share Access")
    object Delete : FolderOptions("Delete File")
}