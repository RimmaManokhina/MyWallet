package com.github.cawboyroy.mywallet.main.presentation

import android.icu.util.Calendar
import android.icu.util.TimeZone
import java.io.Serializable
import java.time.Instant
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

data class MonthsUi(private val now: Long) : Serializable {

    private val timeZone: TimeZone = TimeZone.getDefault()

    fun monthNameAndSum(data: List<FinancialRecordUi>): String {
        val list: List<Double> = data.map { it.sum() }

        val instant = Instant.ofEpochMilli(now)
        val zonedDateTime = instant.atZone(ZoneId.systemDefault())
        val month: Month = zonedDateTime.month
        return month.getDisplayName(
            TextStyle.FULL,
            Locale.getDefault()
        ) + ": " + list.sum()
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
        collapsedDays: Set<Int>,
        records: List<FinancialRecord>,
    ): List<FinancialRecordUi> = if (records.isEmpty())
        emptyList()
    else
        records.map {
            FinancialRecordUi.Base(
                it.money,
                it.title,
                it.category,
                it.description,
                it.time,
                it.isExpenses,
                it.id
            )
        }
            .groupBy { dayOfMonth(it.time) }
            .entries
            .sortedBy { it.key.second }
            .flatMap { (day, dayRecordsUi) ->
                val sum = dayRecordsUi.sumOf { it.money }
                val collapsed: Boolean = collapsedDays.contains(day.second)
                val list: List<FinancialRecordUi> = listOf(
                    if (collapsed)
                        FinancialRecordUi.DayCollapsed(day.first, day.second, sum)
                    else
                        FinancialRecordUi.DayExpanded(day.first, day.second, sum)
                )
                if (collapsed) list else dayRecordsUi + list
            }.reversed()
    //todo make foreach

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