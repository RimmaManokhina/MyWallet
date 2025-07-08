package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)

abstract class CoreBindsModule {

    @Binds
    abstract fun bindsRunAsync(runAsync: RunAsync.Base): RunAsync

    @Binds
    abstract fun bindsChosenCurrency(
        chosenCurrencyRepository: ChosenCurrencyRepository.Base): ChosenCurrencyRepository
}