package com.docubox.util.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.docubox.data.modes.local.FolderOptions
import com.docubox.data.modes.local.StorageItem
import com.docubox.ui.screens.dialogs.FolderOptionsBottomSheetFragment
import com.docubox.util.Constants

fun Fragment.showFolderOptions(
    folder: StorageItem.Folder,
    options: List<FolderOptions>,
    onDelete: (StorageItem.Folder) -> Unit = {},
    onRename: (StorageItem.Folder, String) -> Unit = {_,_->}
) {
    FolderOptionsBottomSheetFragment(options) {
        when (it) {
            FolderOptions.Delete -> handleDeleteFolder(folder, onDelete)
            FolderOptions.Rename -> handleRenameFolder(folder, onRename)
            FolderOptions.RevokeShare -> Unit
            FolderOptions.Share -> Unit
        }
    }.show(childFragmentManager, Constants.FOLDER_OPTION_DIALOG)
}

private fun Fragment.handleDeleteFolder(
    folder: StorageItem.Folder,
    onDelete: (StorageItem.Folder) -> Unit
) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    requireContext().showAlertDialog(
        title = "Delete Folder",
        message = "Are you sure you want to delete this folder?",
        positiveButtonText = "Delete"
    ).also {
        if (it) onDelete(folder)
    }
}

private fun Fragment.handleRenameFolder(
    folder: StorageItem.Folder,
    onRename: (StorageItem.Folder, String) -> Unit
) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
    requireContext().showInputDialog(
        title = "Rename Folder",
        text = folder.name,
        placeholder = "Enter folder name",
        label = "Folder Name"
    ).also {
        if (it.isNotEmpty() && it != folder.name) onRename(folder, it)
    }
}