package com.github.cawboyroy.mywallet.chart.presentation

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.main.presentation.MonthsUi
import java.io.Serializable

data class ChartsScreenState(
    val isExpenses: Boolean,
    val time: MonthsUi,
) : Serializable {

    fun switch(isExpenses: Boolean) = copy(isExpenses = isExpenses)

    fun showPreviousMonth() = copy(time = time.previousMonth())

    fun showNextMonth() = copy(time = time.nextMonth())

    fun monthNameAndSum(data: List<FinancialRecord>, currency: String) =
        time.monthNameAndSumForChart(data, currency)

    fun makeCharts(currency: String, records: List<FinancialRecord>) =
        time.makeCharts(currency, records)
}
