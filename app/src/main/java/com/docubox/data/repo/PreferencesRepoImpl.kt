package com.docubox.data.repo

import com.docubox.data.local.dataSources.dataStore.PreferencesManager
import com.docubox.data.local.models.User
import com.docubox.domain.repo.PreferenceRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Repository for all data store functions
class PreferencesRepoImpl @Inject constructor(private val dataStore: PreferencesManager): PreferenceRepo {

    override fun saveUser(user: User) {
        val serializedUser = Gson().toJson(user) // Convert User object to json
        dataStore.user = serializedUser
    }

    override fun getUser(): User? {
        return Gson().fromJson(dataStore.user, User::class.java) // Convert user json to User object
    }

    override fun removeUser() {
        dataStore.user = null
    }

    override fun observeUser() = dataStore.observeUser().map { Gson().fromJson(it, User::class.java) }

    override fun isUserLoggedIn() = getUser() != null

    override fun getUserToken() = getUser()?.token

    override fun isOnBoardingComplete() = dataStore.onBoarding == true

    override fun setOnBoardingComplete() {
        dataStore.onBoarding = null
    }
}
