package com.github.cawboyroy.mywallet.bars

import kotlinx.collections.immutable.PersistentList
import java.io.Serializable

data class BarsScreenState(
    val isExpenses: Boolean,
    val time: YearsUi
) : Serializable {

    fun showPreviousYear() = copy(time = time.previousYear())

    fun showNextYear() = copy(time = time.nextYear())

    fun switch(isExpenses: Boolean) = copy(isExpenses = isExpenses)

    fun yearAndSum(records: PersistentList<MonthSummaryUi>, currency: String) =
        time.yearAndSum(records, currency)

}