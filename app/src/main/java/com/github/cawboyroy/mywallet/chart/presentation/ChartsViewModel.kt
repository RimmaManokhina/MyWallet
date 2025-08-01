package com.github.cawboyroy.mywallet.chart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import com.github.cawboyroy.mywallet.di.ProvideTime
import com.github.cawboyroy.mywallet.main.data.ListRepository
import com.github.cawboyroy.mywallet.main.presentation.MonthsUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChartsViewModel @Inject constructor(
    provideTime: ProvideTime,
    private val chosenCurrencyRepository: ChosenCurrencyRepository,
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel() {

    fun chosenCurrency() = chosenCurrencyRepository.value()

    val screenStateFlow = savedStateHandle.getStateFlow(
        SCREEN_STATE, ChartsScreenState(
            isExpenses = true,
            time = MonthsUi(provideTime.now()),
        )
    )

    private val recordsMutableStateFlow =
        MutableStateFlow<PersistentList<FinancialRecord>>(persistentListOf())
    val recordsFlow: StateFlow<PersistentList<FinancialRecord>>
        get() = recordsMutableStateFlow

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = screenStateFlow.combine(chosenCurrency()) { screenState, currency ->
                Pair(screenState, currency)
            }.flatMapLatest { (screenState, currency) ->
                val (min, max) = screenState.time.monthBoundaries()
                repository.list(screenState.isExpenses, min, max)
            }.map { it.toPersistentList() }
        ) {
            recordsMutableStateFlow.value = it
        }
    }

    fun switch(isExpenses: Boolean) {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.switch(isExpenses)
    }

    fun showPreviousMonth() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.showPreviousMonth()
    }

    fun showNextMonth() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.showNextMonth()
    }

    companion object {
        private const val SCREEN_STATE = "CHARTS SCREEN_STATE"
    }
}