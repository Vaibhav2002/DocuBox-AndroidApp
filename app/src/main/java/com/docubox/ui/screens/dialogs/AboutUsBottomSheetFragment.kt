package com.docubox.ui.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.docubox.BuildConfig
import com.docubox.databinding.FragmentAboutUsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutUsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAboutUsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val versionName = BuildConfig.VERSION_NAME
        binding.versionCodeTv.text = "Version: $versionName"
    }
}