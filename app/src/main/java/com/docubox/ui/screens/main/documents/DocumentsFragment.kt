package com.docubox.ui.screens.main.documents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.docubox.R
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.ui.adapter.OneAdapter
import com.docubox.util.extensions.askStoragePermission
import com.docubox.util.extensions.compose
import com.docubox.util.extensions.launchAndCollectLatest
import com.docubox.util.viewBinding.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DocumentsFragment : Fragment(R.layout.fragment_documents) {

    private val binding by viewBinding(FragmentDocumentsBinding::bind)
    private val viewModel by viewModels<DocumentsViewModel>()
    private lateinit var storageAdapter: OneAdapter<StorageItem, ItemStorageBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        collectUiState()
    }

    private fun collectUiState() = viewModel.uiState.launchAndCollectLatest(viewLifecycleOwner) {
        storageAdapter.submitList(it.storageItems)
    }

    private fun initViews() = with(binding) {
        fileAndFolderRv.setHasFixedSize(false)
        storageAdapter = fileAndFolderRv.compose(
            ItemStorageBinding::inflate,
            onBind = { item: StorageItem, pos ->
                title.text = item.name
                description.text = item.description
                itemImage.setImageResource(item.icon)
            },
        ) {
        }
    }

    private fun initListeners() = with(binding) {
        addFileBtn.setOnClickListener {
            openBottomSheet()
        }
    }

    private fun openBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            R.style.DocuBox_BottomSheet
        ).apply {
            setContentView(R.layout.sheet_upload_document)
        }.also { it.show() }
    }

    private fun openFilePicker() = lifecycleScope.launchWhenStarted {
        askStoragePermission().also {
            if (it) // open file picker
                Timber.d("All permissions given")
        }
    }
}
