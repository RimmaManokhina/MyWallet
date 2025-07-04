package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.add.data.CacheModule
import com.github.cawboyroy.mywallet.add.data.FinancialRecordsDao
import com.github.cawboyroy.mywallet.add.data.ListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //todo should it be singleton?
class ListModule {

    @Provides
    fun provideDao(cacheModule: CacheModule): FinancialRecordsDao = cacheModule.dao()
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