package com.github.cawboyroy.mywallet.main.presentation

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import java.io.Serializable

data class ScreenState(
    val allCollapsed: AllCollapsedUi,
    val isExpenses: Boolean,
    val time: MonthsUi,
    val collapsedIds: CollapsedIds
) : Serializable {

    fun separatedList(currency: String, records: List<FinancialRecord>) =
        time.separatedList(currency, collapsedIds.value(), records)

    fun switch(isExpenses: Boolean) = copy(isExpenses = isExpenses)

    fun showPreviousMonth() = copy(time = time.previousMonth())

    fun showNextMonth() = copy(time = time.nextMonth())

    fun collapse(id: Int) = copy(collapsedIds = collapsedIds.add(id))

    fun expand(id: Int) = copy(collapsedIds = collapsedIds.remove(id))

    fun monthNameAndSum(data: List<FinancialRecordUi>, currency: String) =
        time.monthNameAndSum(data, currency)

    fun collapseAll() =
        copy(allCollapsed = AllCollapsedUi.Collapsed, collapsedIds = collapsedIds.collapseAll())

    fun expandAll() =
        copy(allCollapsed = AllCollapsedUi.Expanded, collapsedIds = collapsedIds.expandAll())
}