package com.docubox.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

fun <T> runsafe(call: () -> T): Resource<T> = try {
    val result = call()
    Resource.Success(result)
} catch (e: Exception) {
    Timber.d(e.message.toString())
    Resource.Error(message = e.message.toString())
}

suspend fun <T> runSafeAsync(call: () -> T): Resource<T> = withContext(Dispatchers.IO) {
    return@withContext runsafe(call)
}
