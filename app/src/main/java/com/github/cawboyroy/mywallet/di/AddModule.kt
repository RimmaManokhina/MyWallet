package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.add.data.AddRepository
import com.github.cawboyroy.mywallet.add.data.FinancialRecordSuggestionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AddBindsModule {

    @Binds
    abstract fun bindAddRepository(repository: AddRepository.Base): AddRepository

    @Binds
    abstract fun bindFinancialRecordSuggestionsRepository(
        repository: FinancialRecordSuggestionsRepository.Base
    ): FinancialRecordSuggestionsRepository
}