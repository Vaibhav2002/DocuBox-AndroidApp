package com.docubox.data.repo

import com.docubox.data.local.dataSources.dataStore.PreferencesManager
import com.docubox.data.modes.local.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepo @Inject constructor() {

    private val dataStore = PreferencesManager

    fun saveUser(user: User) {
        val serializedUser = Gson().toJson(user)
        dataStore.user = serializedUser
    }

    fun getUser(): User? {
        return Gson().fromJson(dataStore.user, User::class.java)
    }

    fun observeUser() = dataStore.observeUser().map { Gson().fromJson(it, User::class.java) }

    fun isUserLoggedIn() = getUser() != null

    fun getUserToken() = getUser()?.token
}
