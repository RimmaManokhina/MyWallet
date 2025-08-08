package com.github.cawboyroy.mywallet.main.presentation

import android.icu.util.Calendar
import android.icu.util.TimeZone
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import com.github.cawboyroy.mywallet.chart.presentation.FinancialRecordChartUi
import java.io.Serializable
import java.time.Instant
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import java.math.BigDecimal
import java.math.RoundingMode

data class MonthsUi(private val now: Long) : Serializable {

    private val timeZone: TimeZone = TimeZone.getDefault()

    fun monthNameAndSumForChart(
        data: List<FinancialRecord>,
        currency: String
    ): MonthAndTotal {
        val list: List<BigDecimal> = data.map { BigDecimal(it.money) }
        return total(list, currency)
    }

    private fun total(list: List<BigDecimal>, currency: String): MonthAndTotal {
        val instant = Instant.ofEpochMilli(now)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val month: Month = zonedDateTime.month
        var sum = BigDecimal.ZERO
        list.forEach {
            sum = sum.add(it)
        }
        val wholeSum: String = HandleMoney.formatWhole(currency, sum.toString())
        val monthUi: String = month.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        )
        return MonthAndTotal(monthUi, wholeSum)
    }

    fun nextMonth(): MonthsUi {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.DAY_OF_MONTH, 2)
        calendar.add(Calendar.MONTH, 1)
        return MonthsUi(calendar.timeInMillis)
    }

    fun previousMonth(): MonthsUi {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.DAY_OF_MONTH, 2)
        calendar.add(Calendar.MONTH, -1)
        return MonthsUi(calendar.timeInMillis)
    }

    fun monthBoundaries(): Pair<Long, Long> {
        val calendar = Calendar.getInstance(timeZone)

        calendar.timeInMillis = now

        calendar.set(Calendar.DAY_OF_MONTH, 1)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val currentMonthStartMillis = calendar.timeInMillis

        calendar.add(Calendar.MONTH, 1)

        val nextMonthStartMillis = calendar.timeInMillis

        return Pair(currentMonthStartMillis, nextMonthStartMillis)
    }

    fun makeCharts(currency: String, records: List<FinancialRecord>): List<FinancialRecordChartUi> {
        return makeChartsInner(currency, records) { true }
    }

    fun makeCharts(
        currency: String,
        records: List<FinancialRecord>,
        expandedCategory: String
    ): List<FinancialRecordChartUi> {
        return makeChartsInner(currency, records) { it != expandedCategory }
    }

    private fun makeChartsInner(
        currency: String,
        records: List<FinancialRecord>,
        collapsed: (String) -> Boolean
    ): List<FinancialRecordChartUi> {
        if (records.isEmpty())
            return emptyList()
        val result = mutableListOf<FinancialRecordChartUi>()
        val grouped = records.groupBy { it.category }
        val total = records.sumOf { BigDecimal(it.money) }
        grouped.forEach { category, records ->
            val sumOfCategory: BigDecimal = records.sumOf { BigDecimal(it.money) }
            val percentage: Float =
                sumOfCategory.multiply(BigDecimal(100)).divide(total, 2, RoundingMode.HALF_UP)
                    .toFloat()
            result.add(
                FinancialRecordChartUi.CategoryHeader(
                    collapsed(category),
                    currency,
                    category,
                    sumOfCategory.toString(),
                    records.first().isExpenses,
                    percentage
                )
            )
        }
        return result
    }
}

data class MonthAndTotal(val month: String, val total: String)