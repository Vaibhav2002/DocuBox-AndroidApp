package com.docubox.ui.screens.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.docubox.R
import com.docubox.ui.screens.auth.AuthActivity
import com.docubox.ui.screens.main.DocuBoxActivity
import com.docubox.util.extensions.launchAndCollect
import com.docubox.util.extensions.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        collectEvents()
    }

    private fun collectEvents() = viewModel.events.launchAndCollect(this) {
        when (it) {
            SplashScreenEvents.NavigateToAuthScreen -> navigate(AuthActivity::class.java, true)
            SplashScreenEvents.NavigateToHomeScreen -> navigate(DocuBoxActivity::class.java, true)
        }
    }
}
