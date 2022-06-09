package com.docubox.di

import com.docubox.data.local.fileEncryptor.AliceFileEncryptor
import com.docubox.data.local.fileEncryptor.FileEncryptor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalInterfaceModule {

    @Binds
    abstract fun bindsFileEncryptor(
        aliceFileEncryptor: AliceFileEncryptor
    ): FileEncryptor
}
