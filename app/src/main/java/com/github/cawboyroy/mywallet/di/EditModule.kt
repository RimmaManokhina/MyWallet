package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.main.data.EditRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EditModule {

    @Binds
    abstract fun binEditRepository(repository: EditRepository.Base): EditRepository
}