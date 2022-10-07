package com.docubox.data.repo

import com.docubox.data.mapper.toLocal
import com.docubox.data.remote.dataSources.AuthDataSource
import com.docubox.data.remote.models.UserDto
import com.docubox.util.Resource
import com.docubox.util.extensions.mapToUnit
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Repository for all authentication functions
class AuthRepo @Inject constructor(
    private val authDataSource: AuthDataSource, // Class to login and signup user
    private val preferencesRepo: PreferencesRepo, // Repository that contains data store functions
) {

    fun isUserLoggedIn() = preferencesRepo.isUserLoggedIn()

    suspend fun loginUser(
        email: String,
        password: String
    ) = flow {
        // Use resource->Resource.Success to get resource state and resource.data to get resource data
        emit(Resource.Loading())
        val resource = authDataSource.loginUser(email, password)
        if (resource is Resource.Success)
            saveUserLocally(resource.data)
        emit(resource.mapToUnit())
    }

    suspend fun registerUser(
        username: String,
        email: String,
        password: String
    ) = flow {
        emit(Resource.Loading())
        val resource = authDataSource.registerUser(username, email, password)
        if (resource is Resource.Success)
            saveUserLocally(resource.data)
        emit(resource.mapToUnit())
    }

    suspend fun logoutUser() {
        preferencesRepo.removeUser()
    }

    private suspend fun saveUserLocally(userDto: UserDto?) {
        userDto?.let { preferencesRepo.saveUser(it.toLocal()) }
    }
}
