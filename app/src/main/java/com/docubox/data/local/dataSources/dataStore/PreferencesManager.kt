package com.docubox.data.local.dataSources.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.docubox.data.local.dataSources.dataStore.Keys.USER_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private object Keys {
    val USER_KEY = stringPreferencesKey("user")
}

@Singleton
class PreferencesManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    var user by DataStoreManager(
        dataStore = dataStore,
        key = USER_KEY,
        defaultValue = ""
    )

    fun observeUser() = dataStore.data.map { it[USER_KEY] }
}
