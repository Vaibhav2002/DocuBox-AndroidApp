package com.docubox.data.repo

import com.docubox.data.modes.mapper.UserMapper
import com.docubox.data.modes.remote.UserDto
import com.docubox.data.remote.dataSources.AuthDataSource
import com.docubox.util.Resource
import com.docubox.util.extensions.mapToUnit
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Repository for all authentication functions
class AuthRepo @Inject constructor(
    private val authDataSource: AuthDataSource, // Class to login and signup user
    private val preferencesRepo: PreferencesRepo, // Repository that contains data store functions
    private val userMapper: UserMapper // To map data between local and remote
) {

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

    private suspend fun saveUserLocally(userDto: UserDto?) {
        userDto?.let { preferencesRepo.saveUser(userMapper.toLocal(it)) }
    }
}
