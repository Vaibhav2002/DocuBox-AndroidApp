package com.docubox.util

// Resource class for API calling
sealed class Resource<T>(
    open val data: T? = null,
    open val message: String = "",
    open val errorType: ErrorType = ErrorType.Unknown
) {

    class Loading<T>() : Resource<T>()

    data class Success<T>(override val data: T?, override val message: String = "") :
        Resource<T>(data, message)

    data class Error<T>(
        override val errorType: ErrorType = ErrorType.Unknown,
        override val message: String = errorType.errorMessage
    ) : Resource<T>(null, message, errorType)
}

sealed class ErrorType(open val title: String, open val errorMessage: String) {
    object NoInternet : ErrorType(
        "No Internet",
        "Looks like you don't have an active internet connection"
    )

    object Unknown : ErrorType("Unknown Error", "Oops something went wrong")
}
