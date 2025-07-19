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

    fun monthNameAndSum(data: List<FinancialRecordUi>, currency: String): MonthAndTotal {
        val list: List<BigDecimal> = data.map { it.sum() }
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

    fun separatedList(
        currency: String,
        collapsedDays: Set<Int>,
        records: List<FinancialRecord>
    ): List<FinancialRecordUi> {
        if (records.isEmpty()) return emptyList()

        val list = mutableListOf<FinancialRecordUi>()

        var currentDaySum = BigDecimal.ZERO
        var currentDayId = -1
        var currentDayUi = ""
        records.forEachIndexed { index, record ->
            val (recordUi, recordId) = dayOfMonth(record.time)
            if (currentDayId == -1) {
                currentDayId = recordId
                currentDayUi = recordUi
            } else if (recordId != currentDayId) {
                list.add(
                    if (collapsedDays.contains(currentDayId))
                        FinancialRecordUi.DayCollapsed(
                            currency,
                            currentDayUi,
                            currentDayId,
                            currentDaySum
                        )
                    else
                        FinancialRecordUi.DayExpanded(
                            currency,
                            currentDayUi,
                            currentDayId,
                            currentDaySum
                        )
                )
                currentDayId = recordId
                currentDayUi = recordUi
                currentDaySum = BigDecimal.ZERO
            }

            val isCurrentRecordDayCollapsed = collapsedDays.contains(recordId)
            if (!isCurrentRecordDayCollapsed) list.add(
                FinancialRecordUi.Base(
                    currency,
                    record.isExpenses,
                    record.money,
                    record.title,
                    record.category,
                    record.id
                )
            )

            currentDaySum = currentDaySum.add(BigDecimal(record.money))

            if (index == records.size - 1) list.add(
                if (collapsedDays.contains(currentDayId))
                    FinancialRecordUi.DayCollapsed(
                        currency,
                        currentDayUi,
                        currentDayId,
                        currentDaySum
                    )
                else
                    FinancialRecordUi.DayExpanded(
                        currency,
                        currentDayUi,
                        currentDayId,
                        currentDaySum
                    )
            )
        }

        return list.reversed()
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


    private fun dayOfMonth(time: Long): Pair<String, Int> {
        val instant = Instant.ofEpochMilli(time)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val day = zonedDateTime.dayOfMonth
        val month: Month = zonedDateTime.month
        return Pair(
            "${
                month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                )
            } $day", day
        )
    }
}

data class MonthAndTotal(val month: String, val total: String)