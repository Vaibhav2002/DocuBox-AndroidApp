package com.docubox.util

import com.docubox.data.remote.models.MessageResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

// Function to safely make API calls and handle errors using timber and resource class
suspend fun <T> safeApiCall(
    successMessage: String = "",
    errorMessage: String? = null,
    call: suspend () -> Response<T>
): Resource<T> = withContext(Dispatchers.IO) {
    return@withContext try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it, successMessage) }
                ?: Resource.Error(message = errorMessage ?: "DATA NULL")
        } else Resource.Error(
            // git error fixing
            message = errorMessage ?: getMessageFromErrorResponse(response.errorBody())
        )
    } catch (e: IOException) {
        Timber.d(e.toString())
        Resource.Error(ErrorType.NoInternet, message = errorMessage ?: e.message.toString())
    } catch (e: Exception) {
        Timber.d(e.toString())
        Resource.Error(ErrorType.Unknown, message = errorMessage ?: e.message.toString())
    }
}

fun getMessageFromErrorResponse(error: ResponseBody?): String {
    return error?.let { Gson().fromJson(it.charStream(), MessageResponse::class.java).message }
        ?: "Unknown Error Occurred"
}
