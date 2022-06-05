package com.docubox.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal class DiffUtilCallback<ITEM> : DiffUtil.ItemCallback<ITEM>() {

    override fun areItemsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ITEM & Any, newItem: ITEM & Any): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
