package com.docubox.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

// Function to safely call a function and handle errors using timber logging and resource class
fun <T> runSafe(call: () -> T): Resource<T> = try {
    val result = call()
    Resource.Success(result)
} catch (e: Exception) {
    Timber.d(e.message.toString())
    Resource.Error(message = e.message.toString())
}

// Function to call runSafe function asynchronously
suspend fun <T> runSafeAsync(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    call: () -> T
): Resource<T> = withContext(dispatcher) {
    return@withContext runSafe(call)
}
