package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import com.github.cawboyroy.mywallet.main.data.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListViewModel @Inject constructor(
    private val chosenCurrencyRepository: ChosenCurrencyRepository,
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel(), RecordActions, AllDayActions {

    fun chosenCurrency() = chosenCurrencyRepository.value()

    val screenStateFlow = savedStateHandle.getStateFlow(
        SCREEN_STATE, ScreenState(
            allCollapsed = AllCollapsedUi.Expanded,
            isExpenses = true,
            time = MonthsUi(System.currentTimeMillis()),
            collapsedIds = CollapsedIds(emptySet())
        )
    )

    private val recordsMutableStateFlow = MutableStateFlow<List<FinancialRecordUi>>(emptyList())
    val recordsFlow: StateFlow<List<FinancialRecordUi>>
        get() = recordsMutableStateFlow

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = screenStateFlow.combine(chosenCurrency()) { screenState, currency ->
                Pair(screenState, currency)
            }.flatMapLatest { (screenState, currency) ->
                repository.list(screenState.isExpenses, screenState.time)
                    .map { records -> screenState.separatedList(currency, records) }
            }
        ) {
            recordsMutableStateFlow.value = it
        }
    }

    override fun expandAll() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.expandAll()
    }

    override fun collapseAll() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.collapseAll()
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

    override fun collapse(id: Int) {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.collapse(id)
    }

    override fun expand(id: Int) {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.expand(id)
    }

    companion object {
        private const val SCREEN_STATE = "SCREEN_STATE"
    }
}