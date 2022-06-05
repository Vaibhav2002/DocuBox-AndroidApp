package com.docubox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class AbstractAdapter<ITEM : Any, VB : ViewBinding>(
    private val binding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : ListAdapter<ITEM, AbstractAdapter.ViewHolder<VB>>(DiffUtilCallback()) {

    protected abstract fun onItemClick(item: ITEM, view: View)

    protected abstract fun VB.bind(item: ITEM, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val viewHolder = ViewHolder(binding(LayoutInflater.from(parent.context), parent, false))
        viewHolder.binding.root.setOnClickListener {
            val pos = viewHolder.adapterPosition
            if (pos != RecyclerView.NO_POSITION)
                onItemClick(currentList[pos], it)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        holder.binding.bind(currentList[position], position)
    }

    class ViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}
