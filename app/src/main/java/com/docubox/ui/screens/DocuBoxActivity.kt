package com.docubox.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.docubox.R
import com.docubox.databinding.ActivityDocuboxBinding

class DocuBoxActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocuboxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocuboxBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavView.setupWithNavController(navController)
    }
}
