package com.github.cawboyroy.mywallet.chart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class PieChartViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val chosenCurrencyRepository: ChosenCurrencyRepository
) : ViewModel() {

    private val recordsMutableStateFlow =
        MutableStateFlow<PersistentList<FinancialRecordChartUi>>(persistentListOf())
    val recordsFlow: StateFlow<PersistentList<FinancialRecordChartUi>>
        get() = recordsMutableStateFlow

    fun convert(
        screenState: ChartsScreenState,
        records: PersistentList<FinancialRecord>
    ) {
        runAsync.runAsync(viewModelScope, {
            screenState.makeCharts(chosenCurrencyRepository.value().first(), records)
                .sortedByDescending { it.sum() }
                .transformToZigzag()
                .toPersistentList()
        }) {
            recordsMutableStateFlow.value = it
        }
    }
}

private fun <T : Any> List<T>.transformToZigzag(): List<T> {
    if (size < 3)
        return this

    val result = mutableListOf<T>()
    var left = 0
    var right = size - 1
    var takeFromLeft = true

    while (left <= right) {
        if (takeFromLeft) {
            result.add(this[left])
            left++
        } else {
            result.add(this[right])
            right--
        }
        if (left <= right)
            takeFromLeft = !takeFromLeft
    }
    return result
}