package com.docubox.data.repo

import com.docubox.data.local.dataSources.dataStore.PreferencesManager
import com.docubox.data.modes.local.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Repository for all data store functions
class PreferencesRepo @Inject constructor(private val dataStore: PreferencesManager) {

    fun saveUser(user: User) {
        val serializedUser = Gson().toJson(user) // Convert User object to json
        dataStore.user = serializedUser
    }

    fun getUser(): User? {
        return Gson().fromJson(dataStore.user, User::class.java) // Convert user json to User object
    }

    fun removeUser() {
        dataStore.user = null
    }

    fun observeUser() = dataStore.observeUser().map { Gson().fromJson(it, User::class.java) }

    fun isUserLoggedIn() = getUser() != null

    fun getUserToken() = getUser()?.token
}
