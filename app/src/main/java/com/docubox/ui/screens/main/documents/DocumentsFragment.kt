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
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber

class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val binding by viewBinding(FragmentDocumentsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        addFileBtn.setOnClickListener {
            //openFilePicker()
            val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.DocuBox_BottomSheet)
            bottomSheetDialog.setContentView(R.layout.sheet_upload_document)
            bottomSheetDialog.show()
        }
    }

    private fun openFilePicker() = lifecycleScope.launchWhenStarted {
        askStoragePermission().also {
            if (it != null) // open file picker
                Timber.d("All permissions given")
        }
    }
}



