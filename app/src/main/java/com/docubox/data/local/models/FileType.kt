package com.docubox.data.local.models

import androidx.annotation.DrawableRes
import com.docubox.R

// Class to get media type of a file
sealed class FileType(
    val type: String,
    @DrawableRes val icon: Int,
    val mimeType: String,
    val title: String = mimeType
) {
    object Audio : FileType("Audio", R.drawable.ic_audio, "audio", "Audio")
    object Document : FileType("Documents", R.drawable.ic_document, "application/pdf", "Documents")
    object Image : FileType("Image", R.drawable.ic_image, "image", "Images")
    object Video : FileType("Video", R.drawable.ic_video, "video", "Videos")
    object File : FileType("File", R.drawable.ic_document, "application", "Documents")
}
