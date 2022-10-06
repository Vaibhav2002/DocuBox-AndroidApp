package com.docubox.ui.screens.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.docubox.data.local.models.FolderOptions
import com.docubox.databinding.ItemOptionsBinding
import com.docubox.databinding.OptionsBottomSheetBinding
import com.docubox.util.extensions.compose
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderOptionsBottomSheetFragment(
    private val items: List<FolderOptions> = emptyList(),
    private val onOptionSelected: (FolderOptions) -> Unit = {}
) : BottomSheetDialogFragment() {

    private lateinit var binding: OptionsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OptionsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.optionsRv.setHasFixedSize(false)
        binding.optionsRv.compose(
            ItemOptionsBinding::inflate,
            onBind = { item: FolderOptions, pos ->
                text.text = item.text
            },
        ) {
            onOptionSelected(this)
            dismiss()
        }.apply { submitList(items) }
    }
}