package com.docubox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

class OneAdapter<ITEM : Any, VB : ViewBinding>(
    binding: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private val onBind: VB.(ITEM, Int) -> Unit,
    private val itemClick: ITEM.(View) -> Unit
) : AbstractAdapter<ITEM, VB>(binding) {

    override fun onItemClick(item: ITEM, view: View) {
        itemClick(item, view)
    }

    override fun VB.bind(item: ITEM, position: Int) {
        onBind(item, position)
    }
}
