package com.github.cawboyroy.mywallet.chart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.add.presentation.formatDate
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
import kotlin.collections.sortedByDescending
import kotlin.collections.sortedWith

@HiltViewModel
class ChartDetailsViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val chosenCurrencyRepository: ChosenCurrencyRepository,
) : ViewModel() {

    private val recordsMutableStateFlow =
        MutableStateFlow<PersistentList<FinancialRecordChartUi>>(persistentListOf())
    val recordsFlow: StateFlow<PersistentList<FinancialRecordChartUi>>
        get() = recordsMutableStateFlow

    fun changeDetails(
        records: PersistentList<FinancialRecord>,
        clickedCategory: String,
        topCategory: String,
        collapsed: Boolean,
        screenState: ChartsScreenState
    ) {
        if (collapsed) {
            runAsync.runAsync(viewModelScope, {
                val currency: String = chosenCurrencyRepository.value().first()
                val result = mutableListOf<FinancialRecordChartUi>()
                val headers = screenState.time.makeCharts(currency, records, clickedCategory)
                    .sortedByDescending { it.sum() }
                //first I show top category
                headers.find { it.category() == topCategory }?.let { topCategoryHeader ->
                    result.add(topCategoryHeader)
                    //if top category and clicked are the same show details
                    if (topCategory == clickedCategory) {
                        val details = records.filter { topCategory == it.category }
                        result.addAll(details.map {
                            detail(currency, it)
                        })
                    }
                }
                //then I go through not top categories
                headers.filter { it.category() != topCategory }.forEach { nonTopHeader ->
                    result.add(nonTopHeader)
                    //if user clicked on not the top category
                    if (nonTopHeader.category() == clickedCategory) {
                        val details = records.filter { nonTopHeader.category() == it.category }
                        result.addAll(details.map {
                            detail(currency, it)
                        })
                    }
                }
                result.toPersistentList()
            }) {
                recordsMutableStateFlow.value = it
            }
        } else
            load(screenState, records, topCategory)
    }

    private fun detail(currency: String, record: FinancialRecord) =
        FinancialRecordChartUi.RecordDetail(
            currency,
            record.isExpenses,
            record.money,
            record.title,
            record.category,
            record.id,
            formatDate(record.time)
        )

    fun load(
        screenState: ChartsScreenState,
        records: PersistentList<FinancialRecord>,
        topCategory: String
    ) {
        runAsync.runAsync(viewModelScope, {
            val currency: String = chosenCurrencyRepository.value().first()
            val chartsUiList: List<FinancialRecordChartUi> =
                screenState.makeCharts(currency, records)
            (if (topCategory.isEmpty())
                chartsUiList.sortedByDescending { it.sum() }
            else
                chartsUiList.sortedWith(Comparator { aUi, bUi ->
                    val a = aUi.category()
                    val b = bUi.category()
                    when {
                        a == topCategory && b == topCategory -> 0
                        a == topCategory -> -1
                        b == topCategory -> 1
                        else -> if (aUi.sum() > bUi.sum()) -1 else 1
                    }
                })).toPersistentList()
        }) {
            recordsMutableStateFlow.value = it
        }
    }
}