package com.docubox.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.docubox.data.modes.local.StorageItem

// Class for configuring DiffUtil
internal class DiffUtilCallback<ITEM> : DiffUtil.ItemCallback<ITEM>() {

    override fun areItemsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean {
        return when {
            oldItem is StorageItem && newItem is StorageItem -> oldItem.id == newItem.id
            else -> oldItem.hashCode() == newItem.hashCode()
        }
    }
}
