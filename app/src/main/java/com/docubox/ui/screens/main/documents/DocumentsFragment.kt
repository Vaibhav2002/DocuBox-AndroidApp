package com.docubox.ui.screens.main.documents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.docubox.R
import com.docubox.data.modes.local.StorageItem
import com.docubox.databinding.FragmentDocumentsBinding
import com.docubox.databinding.ItemStorageBinding
import com.docubox.databinding.SheetUploadDocumentBinding
import com.docubox.ui.adapter.OneAdapter
import com.docubox.util.extensions.*
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
        initActionBar()
        initViews()
        initListeners()
        collectUiState()
        collectEvents()
    }

    private fun collectEvents() = viewModel.events.launchAndCollect(viewLifecycleOwner) {
        when (it) {
            is DocumentsScreenEvents.ShowToast -> requireContext().showToast(it.message)
        }
    }

    private fun initActionBar() = with(binding) {
        actionBar.setupActionBar("Documents", true, {
            findNavController().popBackStack()
        })
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
        addFileBtn.singleClick(this@DocumentsFragment::openBottomSheet)
    }

    private fun openBottomSheet() {

        val bottomSheetBinding = SheetUploadDocumentBinding.inflate(LayoutInflater.from(requireContext()))

        val bottomSheetDialog = BottomSheetDialog(
            requireContext(),
            R.style.DocuBox_BottomSheet
        ).apply {
            setContentView(bottomSheetBinding.root)
        }.also {
            it.show()
        }

        bottomSheetBinding.btnUploadFile.setOnClickListener {
            bottomSheetDialog.dismiss()
            openFilePicker()
        }

    }

    private val filePickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            Toast.makeText(requireContext(),"File Picker Data received",Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFilePicker() = lifecycleScope.launchWhenStarted {
        askStoragePermission().also {
            if (it) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                filePickerResultLauncher.launch(intent)
            }

        }
    }
}
