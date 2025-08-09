package com.github.cawboyroy.mywallet.main.domain

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import com.github.cawboyroy.mywallet.main.data.ListRepository
import com.github.cawboyroy.mywallet.main.presentation.HomeScreenParams
import com.github.cawboyroy.mywallet.main.presentation.MonthsUi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListInteractor {

    fun list(homeScreenParams: HomeScreenParams): Flow<List<FinancialRecord>>

    fun currency(): Flow<String>

    class Base @Inject constructor(
        private val chosenCurrencyRepository: ChosenCurrencyRepository,
        private val repository: ListRepository,
    ) : ListInteractor {

        override fun list(homeScreenParams: HomeScreenParams): Flow<List<FinancialRecord>> {
            val (min, max) = MonthsUi(homeScreenParams.time).monthBoundaries()
            return repository.list(homeScreenParams.isExpenses, min, max)
        }
        override fun currency() = chosenCurrencyRepository.value()
    }
}