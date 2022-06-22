package com.docubox.ui.screens.main.others

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.docubox.R
import com.docubox.databinding.FragmentViewDocumentBinding
import com.docubox.util.Secrets.BASE_URL
import com.docubox.util.viewBinding.viewBinding
import timber.log.Timber


class ViewDocumentFragment : Fragment(R.layout.fragment_view_document) {

    private val args: ViewDocumentFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentViewDocumentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fileId = args.fileId;
        val documentUrl = BASE_URL + "documents/file/${fileId}"
        Timber.d("ERROR URL: $documentUrl")
        binding.webView.apply {
            settings.builtInZoomControls = true
            settings.supportZoom()
            loadUrl(documentUrl)
        }

    }

}