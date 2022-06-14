package com.docubox

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// Application class of our app which will be used by dagger hilt for dependency injection
@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) // For debugging via logcat messages
    }
}
