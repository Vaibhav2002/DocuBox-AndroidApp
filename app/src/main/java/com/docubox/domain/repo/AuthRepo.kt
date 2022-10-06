package com.docubox.domain.repo

import com.docubox.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    fun isUserLoggedIn(): Boolean

    suspend fun loginUser(
        email: String,
        password: String
    ): Flow<Resource<Unit>>

    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ): Flow<Resource<Unit>>

    suspend fun logoutUser()

}