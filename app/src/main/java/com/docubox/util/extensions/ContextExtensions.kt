package com.docubox.util.extensions

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.docubox.databinding.TextInputDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Function to show an alert dialog
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

// Function to show a toast message
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


suspend fun Context.showInputDialog(
    title: String,
    text: String
) = suspendCoroutine<String> {
    val binding = TextInputDialogBinding.inflate(LayoutInflater.from(this)).apply {
        nameTIET.setText(text)
    }

    MaterialAlertDialogBuilder(this).apply {
        setTitle(title)
        setView(binding.root)
        setPositiveButton("Confirm") { _, _ ->
            it.resume(binding.nameTIET.text.toString())
        }
        setNegativeButton("Cancel") { _, _ ->
            it.resume(text)
        }
        show()
    }
}