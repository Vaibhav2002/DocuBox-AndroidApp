package com.docubox.util.extensions

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.docubox.data.modes.local.FileOption
import com.docubox.data.modes.local.StorageItem
import com.docubox.ui.screens.dialogs.FileOptionsBottomSheetFragment
import com.docubox.util.Constants
import timber.log.Timber
import java.io.File


fun Fragment.showFileOptions(
    file: StorageItem.File,
    options: List<FileOption>,
    onDelete: (StorageItem.File) -> Unit = {},
    onRevokeShare: (StorageItem.File, String) -> Unit = { _, _ -> },
    onShare: (StorageItem.File, String) -> Unit = { _, _ -> },
    onRename: (StorageItem.File, String) -> Unit = { _, _ -> },
    onDownload: (StorageItem.File) -> Unit = {}
) {
    FileOptionsBottomSheetFragment(options) {
        when (it) {
            FileOption.Delete -> handleDeleteFile(file, onDelete)
            FileOption.Rename -> Timber.d("Rename File")
            FileOption.RevokeShare -> handleRevokeShareFile(file, onRevokeShare)
            FileOption.Share -> handleShareFile(file, onShare)
            FileOption.Download -> handleDownloadFile(file)
        }
    }.show(childFragmentManager, Constants.FILE_OPTION_DIALOG)
}

private fun Fragment.handleShareFile(
    file: StorageItem.File,
    onShare: (StorageItem.File, String) -> Unit
) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    requireContext().showInputDialog(
        title = "Enter email",
        placeholder = "Enter email",
        label = "Email"
    ).also {
        if (it.isEmpty()) return@also
        onShare(file, it)
    }
}

private fun Fragment.handleRevokeShareFile(
    file: StorageItem.File,
    onRevokeShare: (StorageItem.File, String) -> Unit
) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    requireContext().showSelectItemDialog(
        title = "Select User",
        items = file.file.fileSharedTo
    ).also { email ->
        email?.let { onRevokeShare(file, it) }
    }
}

private fun Fragment.handleDeleteFile(
    file: StorageItem.File,
    onDelete: (StorageItem.File) -> Unit
) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    requireContext().showAlertDialog(
        title = "Delete File",
        message = "Are you sure you want to delete this file?",
        positiveButtonText = "Delete"
    ).also {
        if (it) onDelete(file)
    }
}

fun Fragment.handleDownloadFile(file: StorageItem.File) {
    try {
        val manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val uri = Uri.parse(file.file.fileStorageUrl)
        val request = DownloadManager.Request(uri)
        request.setTitle(file.file.fileName)
        request.setDescription("Downloading")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.file.fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        manager!!.enqueue(request)
    } catch(e: Exception) {
        Timber.d("DOWNLOAD ERROR: ${e.message}")
    }
}