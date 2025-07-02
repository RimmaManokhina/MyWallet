package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.add.data.CacheModule
import com.github.cawboyroy.mywallet.add.data.ExpensesDao
import com.github.cawboyroy.mywallet.add.data.ListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ListModule {

    @Provides
    fun provideDao(cacheModule: CacheModule): ExpensesDao = cacheModule.dao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ListBindModule {

    @Binds
    abstract fun bindListRepository(repository: ListRepository.Base): ListRepository

    @Singleton
    @Binds
    abstract fun bindCacheModule(cacheModule: CacheModule.Base): CacheModule
}