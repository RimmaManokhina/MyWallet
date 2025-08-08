package com.github.cawboyroy.mywallet.main.presentation

import android.icu.util.Calendar
import android.icu.util.TimeZone
import java.io.Serializable

data class HomeScreenParams(
    val allCollapsed: Boolean,
    val time: Long,
    val isExpenses: Boolean,
    val collapsedIds: CollapsedIds,
) : Serializable {

    fun expandAll() = copy(allCollapsed = false, collapsedIds = collapsedIds.expandAll())
    fun collapseAll() = copy(allCollapsed = true, collapsedIds = collapsedIds.collapseAll())
    fun switch(isExpenses: Boolean) = copy(isExpenses = isExpenses)
    fun collapse(id: Int) = copy(collapsedIds = collapsedIds.add(id))
    fun expand(id: Int) = copy(collapsedIds = collapsedIds.remove(id))
    fun showPreviousMonth(): HomeScreenParams {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = time
        calendar.set(Calendar.DAY_OF_MONTH, 2)
        calendar.add(Calendar.MONTH, -1)
        return copy(time = calendar.timeInMillis)
    }

    fun showNextMonth(): HomeScreenParams {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.timeInMillis = time
        calendar.set(Calendar.DAY_OF_MONTH, 2)
        calendar.add(Calendar.MONTH, 1)
        return copy(time = calendar.timeInMillis)
    }
}