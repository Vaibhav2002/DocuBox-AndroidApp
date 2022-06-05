package com.docubox.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.docubox.R
import com.docubox.databinding.ActivityDocuboxBinding
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocuBoxActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDocuboxBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavView.setupWithNavController(navController)
    }
}
