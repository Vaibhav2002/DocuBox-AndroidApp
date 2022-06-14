package com.docubox.util.extensions

import android.app.Activity
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import com.docubox.R
import com.docubox.util.Constants
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Function to navigate to another activity
fun Activity.navigate(destination: Class<*>, finishOff: Boolean = false) {
    Intent(this, destination).also {
        startActivity(it)
        if (finishOff) finish()
    }
}

// Function to ask for storage permissions
suspend fun Fragment.askStoragePermission() = suspendCoroutine {
    PermissionX.init(this)
        .permissions(Constants.filePermissions)
        .onExplainRequestReason { scope, deniedList ->
            scope.showRequestReasonDialog(
                deniedList,
                getString(R.string.rationale_message),
                getString(R.string.ok),
                getString(R.string.cancel)
            )
        }
        .onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                getString(R.string.permission_settings_message),
                getString(R.string.ok),
                getString(R.string.cancel)
            )
        }
        .request { allGranted, grantedList, deniedList ->
            it.resume(allGranted)
        }
}

// Function to show get datastore value
fun <T> DataStore<Preferences>.get(
    key: Preferences.Key<T>,
    defaultValue: T
): T = runBlocking {
    data.first()[key] ?: defaultValue
}

// Function to show set datastore value
fun <T> DataStore<Preferences>.set(
    key: Preferences.Key<T>,
    value: T?
) = runBlocking<Unit> {
    edit {
        if (value == null) {
            it.remove(key)
        } else {
            it[key] = value
        }
    }
}
