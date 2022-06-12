package com.docubox.data.local.dataSources.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.docubox.data.local.dataSources.dataStore.Keys.USER_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private object Keys {
    val USER_KEY = stringPreferencesKey("user")
}

object PreferencesManager {

    @Inject
    lateinit var prefDataStore: DataStore<Preferences>

    var user by DataStoreManager(
        dataStore = prefDataStore,
        key = USER_KEY,
        defaultValue = ""
    )

    fun observeUser() = prefDataStore.data.map { it[USER_KEY] }
}
