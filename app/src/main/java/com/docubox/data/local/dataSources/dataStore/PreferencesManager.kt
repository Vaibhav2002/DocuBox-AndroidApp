package com.docubox.data.local.dataSources.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.docubox.data.local.dataSources.dataStore.Keys.ON_BOARDING_KEY
import com.docubox.data.local.dataSources.dataStore.Keys.USER_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// An object to store data store configurations
private object Keys {
    val USER_KEY = stringPreferencesKey("user")
    val ON_BOARDING_KEY = booleanPreferencesKey("OnBoarding")
}

// Class to handle data store
@Singleton
class PreferencesManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    // Assign the user by putting details in the DataStoreManager.kt wrapper class
    var user: String? by DataStoreManager(
        dataStore = dataStore,
        key = USER_KEY,
        defaultValue = ""
    )

    var onBoarding: Boolean? by DataStoreManager(
        dataStore = dataStore,
        key = ON_BOARDING_KEY,
        defaultValue = false
    )

    // Get the data of that user in datastore
    fun observeUser() = dataStore.data.map { it[USER_KEY] }
}
