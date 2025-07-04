package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.main.data.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel() {

    val timeState =
        savedStateHandle.getStateFlow(TIME, MonthsUi(System.currentTimeMillis()))

    val isExpensesState = savedStateHandle.getStateFlow(IS_EXPENSES, true)

    private val mutableState = MutableStateFlow<List<FinancialRecordUi>>(emptyList())
    val state: StateFlow<List<FinancialRecordUi>>
        get() = mutableState

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = isExpensesState
                .combine(timeState) { a, b -> Pair(a, b) }
                .flatMapLatest { (isExpenses, time) ->
                    repository.list(isExpenses, time)
                }
        ) {
            mutableState.value = timeState.value.separatedList(it) //todo move to flow part later
        }
    }

    fun update(isExpenses: Boolean) {
        savedStateHandle[IS_EXPENSES] = isExpenses
    }

    fun showPreviousMonth() {
        savedStateHandle[TIME] = timeState.value.previousMonth()
    }

    fun showNextMonth() {
        savedStateHandle[TIME] = timeState.value.nextMonth()
    }

    companion object {
        private const val IS_EXPENSES = "isExpensesFlow"
        private const val TIME = "time"
    }
}
