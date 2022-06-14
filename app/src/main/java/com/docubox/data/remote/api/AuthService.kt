package com.docubox.data.remote.api

import com.docubox.data.modes.remote.UserDto
import com.docubox.data.modes.remote.requests.LoginRequest
import com.docubox.data.modes.remote.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// An interface to provide authentication functions for logging in and signing up a user
interface AuthService {

    @POST("auth/login") // API endpoints are specified using Retrofit's @POST annotation
    suspend fun loginUser(
        @Body request: LoginRequest // Attach LoginRequest as a parameter
    ): Response<UserDto> // Return a retrofit Response object with UserDto

    @POST("auth/signup")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<UserDto>
}
