package com.docubox.util.extensions

import com.docubox.util.Resource

// Extension functions for resource class

inline infix fun <T, F> Resource<T>.mapTo(change: (T) -> F): Resource<F> = when (this) {
    is Resource.Error -> Resource.Error(errorType, message)
    is Resource.Loading -> Resource.Loading()
    is Resource.Success -> Resource.Success(data?.let { change(it) }, message)
}

fun Resource<*>.mapToUnit() = this mapTo {}

fun <T> Resource<T>.mapMessages(
    successMessage: String? = null,
    errorMessage: String? = null
): Resource<T> = when (this) {
    is Resource.Error -> copy(message = errorMessage ?: message)
    is Resource.Success -> copy(message = successMessage ?: message)
    else -> this
}
