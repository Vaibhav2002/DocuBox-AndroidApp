package com.docubox.data.local.dataSources

import com.docubox.data.modes.local.StorageItem
import java.util.*

data class CacheData(
    val items: List<StorageItem>,
    val directory: String?,
    val folderName: String,
)

object StorageCache {

    private val cached = Stack<CacheData>()
    private val cachedDirectories = Stack<String?>()

    fun addToCache(data: CacheData) {
        cached.push(data)
    }

    fun popCache(): CacheData = cached.pop()

    fun isEmpty() = cached.empty()

    fun clearCache() = cached.clear()

}