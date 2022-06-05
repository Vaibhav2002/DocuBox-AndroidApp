package com.docubox.util.extensions

import android.app.Activity
import android.content.Intent

fun Activity.navigate(destination: Class<*>, finishOff: Boolean = false) {
    Intent(this, destination).also {
        startActivity(it)
        if (finishOff) finish()
    }
}
