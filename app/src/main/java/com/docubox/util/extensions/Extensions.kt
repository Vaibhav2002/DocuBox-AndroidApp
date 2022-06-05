package com.docubox.util.extensions

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.docubox.R
import com.docubox.util.Constants
import com.permissionx.guolindev.PermissionX
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Activity.navigate(destination: Class<*>, finishOff: Boolean = false) {
    Intent(this, destination).also {
        startActivity(it)
        if (finishOff) finish()
    }
}

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
