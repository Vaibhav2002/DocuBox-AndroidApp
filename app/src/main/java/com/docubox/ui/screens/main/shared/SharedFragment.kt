package com.docubox.ui.screens.main.shared

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.databinding.FragmentSharedBinding
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharedFragment : Fragment(R.layout.fragment_shared) {

    private val binding by viewBinding(FragmentSharedBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()

    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Shared", true, {
            findNavController().popBackStack()
        })
    }


}
