package com.docubox.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.docubox.ui.adapter.OneAdapter
import kotlinx.coroutines.*

// Function to launch an onclick listener
fun View.singleClick(onClick: () -> Unit) {
    setOnClickListener {
        onClick()
    }
}

// Function to toggle visibility of a view
fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

// Function to launch an onclick listener after a delay
fun View.setOnDelayClickListener(delayTime: Long, onClick: () -> Unit) {
    setOnLongClickListener {
        CoroutineScope(Dispatchers.IO).launch {
            delay(delayTime)
            withContext(Dispatchers.Main) {
                if (it.isPressed) {
                    onClick()
                }
            }
        }
        true
    }
}

fun <ITEM : Any, VB : ViewBinding> RecyclerView.compose(
    layout: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    onBind: VB.(ITEM, Int) -> Unit,
    itemClick: ITEM.(View) -> Unit = {}
): OneAdapter<ITEM, VB> {
    return OneAdapter(layout, onBind, itemClick).also { adapter = it }
}