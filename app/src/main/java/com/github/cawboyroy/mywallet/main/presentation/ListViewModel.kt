package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.main.data.ListRepository
import com.github.cawboyroy.mywallet.core.RunAsync
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

    private val mutableState = MutableStateFlow<List<FinancialRecord>>(emptyList())
    val state: StateFlow<List<FinancialRecord>>
        get() = mutableState

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = isExpensesState
                .combine(timeState) { a, b -> Pair(a, b) }
                .flatMapLatest { repository.list(it.first, it.second) }
        ) {
            mutableState.value = it
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
