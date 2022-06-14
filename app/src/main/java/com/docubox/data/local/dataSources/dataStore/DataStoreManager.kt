package com.docubox.data.local.dataSources.dataStore

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.docubox.util.extensions.get
import com.docubox.util.extensions.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// A wrapper class to store data in data store
class DataStoreManager<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) : ReadWriteProperty<Any, T> {

    // A special thread for performing datastore operations
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        dataStore.get(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        dataStore.set(key, value)
    }
}
