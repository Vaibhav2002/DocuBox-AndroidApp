package com.docubox.domain.repo

import com.docubox.data.local.models.User
import kotlinx.coroutines.flow.Flow

interface PreferenceRepo {

    fun saveUser(user: User)

    fun getUser(): User?

    fun removeUser()

    fun observeUser(): Flow<User>

    fun isUserLoggedIn(): Boolean

    fun getUserToken(): String?

    fun isOnBoardingComplete(): Boolean

    fun setOnBoardingComplete()

}