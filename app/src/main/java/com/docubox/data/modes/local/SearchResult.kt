package com.docubox.data.modes.local

import java.io.Serializable

data class SearchResult(
    val results: List<StorageItem.File>
):Serializable
