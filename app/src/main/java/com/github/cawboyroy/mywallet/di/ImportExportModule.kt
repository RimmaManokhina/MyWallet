package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.settings.data.Encryption
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ImportExportModule {

    @Binds
    abstract fun bindEncryption(encryption: Encryption.Base): Encryption
}