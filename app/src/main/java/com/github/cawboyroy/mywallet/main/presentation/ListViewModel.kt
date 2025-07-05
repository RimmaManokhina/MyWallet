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
import java.io.Serializable
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel(), RecordActions {

    val collapsedIds = savedStateHandle.getStateFlow(COLLAPSED, CollapsedIds(mutableSetOf()))

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
                .combine(timeState) { isExpense, time -> Pair(isExpense, time) }
                .flatMapLatest { (isExpenses, time) ->
                    repository.list(isExpenses, time)
                }.combine(collapsedIds) { a, b -> Pair(a, b) }
        ) { (list, collapsed) ->
            mutableState.value = timeState.value.separatedList(
                collapsed.value(),
                list
            ) //todo move to flow part later}
        }
    }

    fun update(isExpenses: Boolean) {
        savedStateHandle[IS_EXPENSES] = isExpenses
    }

    fun showPreviousMonth() {
        savedStateHandle[COLLAPSED] = collapsedIds.value.clear()
        savedStateHandle[TIME] = timeState.value.previousMonth()
    }

    fun showNextMonth() {
        savedStateHandle[COLLAPSED] = collapsedIds.value.clear()
        savedStateHandle[TIME] = timeState.value.nextMonth()
    }

    override fun collapse(id: Int) {
        savedStateHandle[COLLAPSED] = collapsedIds.value.add(id)
    }

    override fun expand(id: Int) {
        savedStateHandle[COLLAPSED] = collapsedIds.value.remove(id)
    }

    companion object {
        private const val IS_EXPENSES = "isExpensesFlow"
        private const val TIME = "time"
        private const val COLLAPSED = "collapsed"
    }
}

data class CollapsedIds(private val set: Set<Int>) : Serializable {

    fun clear(): CollapsedIds {
        return CollapsedIds(emptySet())
    }

    fun add(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.add(day) }.toSet())
    }

    fun remove(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.remove(day) }.toSet())
    }

    fun value(): Set<Int> = set
}