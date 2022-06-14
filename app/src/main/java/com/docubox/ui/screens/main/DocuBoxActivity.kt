package com.docubox.ui.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.docubox.R
import com.docubox.databinding.ActivityDocuboxBinding
import com.docubox.util.extensions.visibleOrGone
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocuBoxActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDocuboxBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragment)
        setUpBottomNav()
    }

    private fun setUpBottomNav() = binding.apply {
        bottomNavView.setupWithNavController(navController)
        val hiddenInFragments = listOf(R.id.documentsFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavView.visibleOrGone(destination.id !in hiddenInFragments)
        }
    }
}
