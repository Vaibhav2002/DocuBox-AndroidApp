package com.docubox.ui.screens.auth.gettingStarted

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.databinding.FragmentGettingStartedBinding
import com.docubox.util.extensions.singleClick
import com.docubox.util.viewBinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GettingStartedFragment : Fragment(R.layout.fragment_getting_started) {

    private val binding by viewBinding(FragmentGettingStartedBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        loginBtn.singleClick { findNavController().navigate(R.id.action_gettingStartedFragment_to_loginFragment) }
        registerBtn.singleClick { findNavController().navigate(R.id.action_gettingStartedFragment_to_registerFragment) }
    }
}
