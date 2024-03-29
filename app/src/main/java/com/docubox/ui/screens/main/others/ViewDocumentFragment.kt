package com.docubox.ui.screens.main.others

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.docubox.R
import com.docubox.data.remote.models.responses.file.FileDto
import com.docubox.databinding.FragmentViewDocumentBinding
import com.docubox.util.Secrets.BASE_URL
import com.docubox.util.extensions.setupActionBar
import com.docubox.util.viewBinding.viewBinding


class ViewDocumentFragment : Fragment(R.layout.fragment_view_document) {

    private val args: ViewDocumentFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentViewDocumentBinding::bind)
    private lateinit var file: FileDto

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        file = args.file.file
        initActionBar()
        initWebView()
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar(
            title = "Document Viewer",
            backButtonEnabled = true,
            backButtonOnClickListener = { findNavController().popBackStack() }
        )
    }

    private fun initWebView() = with(binding) {
        webView.apply {
            settings.builtInZoomControls = true
            settings.supportZoom()
            loadUrl("${BASE_URL}documents/file/${file.id}")
        }
    }

}