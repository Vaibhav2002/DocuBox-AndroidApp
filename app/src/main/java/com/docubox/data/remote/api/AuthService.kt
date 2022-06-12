package com.docubox.data.remote.api

import com.docubox.data.modes.remote.UserDto
import com.docubox.data.modes.remote.requests.LoginRequest
import com.docubox.data.modes.remote.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<UserDto>

    @POST("auth/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<UserDto>
}
