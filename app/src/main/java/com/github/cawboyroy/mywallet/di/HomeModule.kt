package com.github.cawboyroy.mywallet.di

import com.github.cawboyroy.mywallet.main.domain.ListInteractor
import com.github.cawboyroy.mywallet.main.presentation.HomeUiStateMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindInteractor(interactor: ListInteractor.Base): ListInteractor

    @Binds
    abstract fun bindMapper(mapper: HomeUiStateMapper.Base): HomeUiStateMapper
}