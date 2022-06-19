package com.docubox.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.docubox.R
import com.docubox.ui.adapter.OneAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*

// Function to launch an onclick listener
fun View.singleClick(onClick: () -> Unit) {
    setOnClickListener {
        onClick()
    }
}

// Function to call a function after edit text value changes
fun EditText.listenAfterChange(afterChange: (String) -> Unit) {
    doAfterTextChanged { afterChange(it.toString()) }
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

// Function to compose the recyclerview using OneAdapter class which we created
fun <ITEM : Any, VB : ViewBinding> RecyclerView.compose(
    layout: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    onBind: VB.(ITEM, Int) -> Unit,
    itemClick: ITEM.(View) -> Unit = {}
): OneAdapter<ITEM, VB> {
    return OneAdapter(layout, onBind, itemClick).also { adapter = it }
}

fun MaterialButton.setSelectedState(isSelected: Boolean) {
    setBackgroundColor(
        resources.getColor(
            if (isSelected) R.color.colorPrimary else R.color.colorAppBackground,
        )
    )
    setTextColor(
        resources.getColor(
            if (isSelected) R.color.colorOnPrimary else R.color.colorPrimaryText,
        )
    )
}