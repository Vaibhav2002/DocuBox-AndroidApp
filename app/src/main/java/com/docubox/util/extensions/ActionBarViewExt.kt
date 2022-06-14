package com.docubox.util.extensions

import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.docubox.R
import com.docubox.databinding.ActionBarBinding

fun ActionBarBinding.setupActionBar(
    title: String,
    backButtonEnabled: Boolean = false,
    backButtonOnClickListener: () -> Unit = {},
    showMenuButton: Boolean = false,
    @DrawableRes menuButtonIcon: Int = R.drawable.ic_grid,
    menuButtonOnClickListener: () -> Unit = {}
) {
    tvActionBarTitle.text = title
    btnBack.isVisible = backButtonEnabled
    btnBack.setOnClickListener {
        backButtonOnClickListener()
    }
    btnMenuItem.isVisible = showMenuButton
    btnMenuItem.setImageResource(menuButtonIcon)
    btnMenuItem.setOnClickListener {
        menuButtonOnClickListener()
    }

}