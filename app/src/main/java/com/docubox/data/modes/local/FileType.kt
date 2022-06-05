package com.docubox.data.modes.local

import androidx.annotation.DrawableRes
import com.docubox.R

sealed class FileType(
    val type: String,
    @DrawableRes val icon: Int
) {
    object Audio : FileType("Audio", R.drawable.ic_audio)
    object Document : FileType("Documents", R.drawable.ic_document)
    object Image : FileType("Image", R.drawable.ic_image)
    object Video : FileType("Video", R.drawable.ic_video)
    object File : FileType("File", R.drawable.ic_file)
}
