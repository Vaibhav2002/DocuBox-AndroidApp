package com.docubox.util.extensions

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.showAlertDialog(
    title: String,
    message: String = "",
    positiveButtonText: String,
    negativeButtonText: String = "Cancel",
) = suspendCoroutine<Boolean> {
    val dialog = MaterialAlertDialogBuilder(this).apply {
        setTitle(title)
        if (message.isNotEmpty())
            setMessage(message)
        setPositiveButton(positiveButtonText) { _, _ ->
            it.resume(true)
        }
        setNegativeButton(negativeButtonText) { _, _ ->
            it.resume(false)
        }
    }
    dialog.show()
}
