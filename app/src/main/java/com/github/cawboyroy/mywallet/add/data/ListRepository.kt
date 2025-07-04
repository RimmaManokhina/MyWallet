package com.github.cawboyroy.mywallet.add.data

import android.icu.util.Calendar
import android.icu.util.TimeZone
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ListRepository {

    fun list(isExpenses: Boolean, time: Long): Flow<List<FinancialRecord>>

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : ListRepository {

        override fun list(isExpenses: Boolean, time: Long): Flow<List<FinancialRecord>> {
            val boundaries = getMonthBoundariesInMillis(time)
            val min: Long = boundaries.currentMonthStartMillis
            val max: Long = boundaries.nextMonthStartMillis
            return dao.financialRecords(isExpenses, min, max).map { list ->
                list.map {
                    FinancialRecord(
                        it.money,
                        it.title,
                        it.category,
                        it.description,
                        it.time,
                        it.isExpenses,
                        it.id,
                    )
                }
            }
        }
    }
}

data class MonthBoundaries(
    val currentMonthStartMillis: Long,
    val nextMonthStartMillis: Long
)

fun getMonthBoundariesInMillis(
    inputTimeMillis: Long,
    timeZone: TimeZone = TimeZone.getDefault()
): MonthBoundaries {
    val calendar = Calendar.getInstance(timeZone)

    // --- Calculate Start of Current Month ---
    calendar.timeInMillis = inputTimeMillis // Set the calendar to the given time

    // Set to the first day of the month
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // Set time to midnight (00:00:00.000)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val currentMonthStartMillis = calendar.timeInMillis

    // --- Calculate Start of Next Month ---
    // No need to reset to inputTimeMillis, calendar is already at the start of the current month.
    // Simply add one month to the current date (which is already 1st day, 00:00:00)
    calendar.add(Calendar.MONTH, 1)
    // The day, hour, minute, second, millisecond will remain 1, 0, 0, 0, 0 respectively
    // because we are adding a full month.

    val nextMonthStartMillis = calendar.timeInMillis

    return MonthBoundaries(currentMonthStartMillis, nextMonthStartMillis)
}
