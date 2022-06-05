package com.docubox.ui.screens.main.documents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.viewModels
import com.docubox.R
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.util.extensions.askStoragePermission
import com.docubox.util.viewBinding.viewBinding
import timber.log.Timber

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val binding by viewBinding(FragmentDocumentsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.util.viewBinding.viewBinding

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val binding by viewBinding(FragmentDocumentsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    private fun initListeners() = with(binding) {
        addFileBtn.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() = lifecycleScope.launchWhenStarted {
        askStoragePermission().also {
            if (it) // open file picker
                Timber.d("All permissions given")
        }
    }
}

    }
}
