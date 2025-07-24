package com.github.cawboyroy.mywallet.bars

import android.icu.util.Calendar
import android.icu.util.TimeZone
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import kotlinx.collections.immutable.PersistentList
import java.io.Serializable
import java.math.BigDecimal

data class YearsUi(private val now: Long) : Serializable {
    private val timeZone: TimeZone = TimeZone.getDefault()

    fun previousYear(): YearsUi {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 1)
        calendar.add(Calendar.YEAR, -1)
        return YearsUi(calendar.timeInMillis)
    }

    fun nextYear(): YearsUi {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, 1)
        calendar.add(Calendar.YEAR, 1)
        return YearsUi(calendar.timeInMillis)
    }

    fun yearBoundaries(): Pair<Long, Long> {
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentYearFirstMoment = calendar.timeInMillis

        calendar.set(Calendar.MONTH, 11)
        calendar.set(Calendar.DAY_OF_MONTH, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val currentYearLastMoment = calendar.timeInMillis

        return Pair(currentYearFirstMoment, currentYearLastMoment)
    }

    fun yearAndSum(records: PersistentList<MonthSummaryUi>, currency: String): YearAndTotal {
        val sum = records.sumOf { BigDecimal(it.sum) }
        val calendar = Calendar.getInstance(timeZone)
        calendar.timeInMillis = now
        val year = calendar.get(Calendar.YEAR)
        return YearAndTotal(year.toString(), HandleMoney.formatWhole(currency, sum.toString()))
    }
}

data class YearAndTotal(val year: String, val total: String)
