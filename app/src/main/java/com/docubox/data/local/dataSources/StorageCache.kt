package com.docubox.data.local.dataSources

import com.docubox.data.modes.local.StorageItem
import java.util.*

object StorageCache {

    private val cached = Stack<List<StorageItem>>()

    fun addToCache(items: List<StorageItem>) {
        cached.push(items)
    }

    fun popCache(): List<StorageItem> = cached.pop()

    fun isEmpty() = cached.empty()

    fun clearCache() = cached.clear()

}