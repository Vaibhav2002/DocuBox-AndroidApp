package com.docubox.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.docubox.data.local.dataSources.dataStore.PreferencesManager
import com.docubox.data.remote.dataSources.AuthDataSource
import com.docubox.data.remote.dataSources.StorageDataSource
import com.docubox.data.repo.AuthRepoImpl
import com.docubox.data.repo.PreferencesRepoImpl
import com.docubox.data.repo.StorageRepoImpl
import com.docubox.domain.repo.AuthRepo
import com.docubox.domain.repo.PreferenceRepo
import com.docubox.domain.repo.StorageRepo
import com.docubox.util.Constants.DATASTORE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

// Module for providing local data store instance
@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
            migrations = listOf(SharedPreferencesMigration(context, DATASTORE_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
        )

    @Provides
    @Singleton
    fun providesAuthRepo(
        authDataSource: AuthDataSource,
        preferencesRepo: PreferenceRepo
    ): AuthRepo =
        AuthRepoImpl(authDataSource, preferencesRepo)

    @Provides
    @Singleton
    fun providesPreferenceRepo(
        preferencesManager: PreferencesManager
    ): PreferenceRepo =
        PreferencesRepoImpl(preferencesManager)

    @Provides
    @Singleton
    fun providesStorageRepo(
        storageDataSource: StorageDataSource,
        preferencesRepo: PreferenceRepo
    ): StorageRepo =
        StorageRepoImpl(storageDataSource, preferencesRepo)
}