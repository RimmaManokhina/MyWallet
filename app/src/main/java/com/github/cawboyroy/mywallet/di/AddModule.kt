package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.main.data.AddRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AddBindsModule {

    @Binds
    abstract fun bindAddRepository(repository: AddRepository.Base): AddRepository
}