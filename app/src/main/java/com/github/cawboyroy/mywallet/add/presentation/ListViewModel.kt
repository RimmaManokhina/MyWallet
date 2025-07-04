package com.github.cawboyroy.mywallet.add.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.data.ListRepository
import com.github.cawboyroy.mywallet.core.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel() {

    val isExpensesState = savedStateHandle.getStateFlow(IS_EXPENSES, true)

    private val mutableState = MutableStateFlow<List<FinancialRecord>>(emptyList())
    val state: StateFlow<List<FinancialRecord>>
        get() = mutableState

    private var job: Job? = null

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = isExpensesState.flatMapLatest(transform = repository::list)
        ) {
            mutableState.value = it
        }
    }

    fun update(isExpenses: Boolean) {
        savedStateHandle[IS_EXPENSES] = isExpenses
    }

    companion object {
        private const val IS_EXPENSES = "isExpensesFlow"
    }
}