package com.docubox

import android.app.Application
import com.docubox.util.NotificationHelper
import dagger.hilt.android.HiltAndroidApp
import net.gotev.uploadservice.UploadServiceConfig
import timber.log.Timber

// Application class of our app which will be used by dagger hilt for dependency injection
@HiltAndroidApp
class MyApp : Application() {

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) // For debugging via logcat messages
        notificationHelper = NotificationHelper(this)
        notificationHelper.setNotificationChannel()
        UploadServiceConfig.initialize(
            this, NotificationHelper.CHANNEL_ID, true
        )
    }
}
