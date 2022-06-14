package com.docubox.ui.screens.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.databinding.FragmentHomeBinding
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Home")
    }

}
