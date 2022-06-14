package com.docubox.data.local.dataSources.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.docubox.data.local.dataSources.dataStore.Keys.USER_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// An object to store data store configurations
private object Keys {
    val USER_KEY = stringPreferencesKey("user")
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

    // Get the data of that user in datastore
    fun observeUser() = dataStore.data.map { it[USER_KEY] }
}
