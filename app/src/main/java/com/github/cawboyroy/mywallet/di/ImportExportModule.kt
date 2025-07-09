package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.settings.data.Encryption
import com.github.cawboyroy.mywallet.settings.data.ImportExportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ImportExportModule {

    @Binds
    abstract fun bindEditRepository(repository: ImportExportRepository.Base): ImportExportRepository

    @Binds
    abstract fun bindEncryption(encryption: Encryption.Base): Encryption
}