package com.github.cawboyroy.mywallet.main.presentation

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal
import java.time.Instant
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

interface HomeUiStateMapper {

    suspend fun map(
        list: List<FinancialRecord>,
        homeScreenParams: HomeScreenParams,
        currency: String
    ): HomeScreenState

    class Base @Inject constructor(
    ) : HomeUiStateMapper {

        override suspend fun map(
            list: List<FinancialRecord>,
            homeScreenParams: HomeScreenParams,
            currency: String
        ): HomeScreenState {
            val collapsedDays = homeScreenParams.collapsedIds.value()
            val recordsUi = separatedList(currency, collapsedDays, list)
            val monthAndTotal = monthNameAndSum(homeScreenParams.time, recordsUi, currency)
            return HomeScreenState(
                homeScreenParams.time,
                homeScreenParams.isExpenses,
                monthAndTotal.month,
                monthAndTotal.total,
                if (homeScreenParams.allCollapsed) AllCollapsedUi.Collapsed else AllCollapsedUi.Expanded,
                recordsUi.toPersistentList(),
            )
        }

        private fun separatedList(
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

        private fun monthNameAndSum(
            now: Long,
            data: List<FinancialRecordUi>,
            currency: String
        ): MonthAndTotal {
            val list: List<BigDecimal> = data.map { it.sum() }
            return total(now, list, currency)
        }

        private fun total(now: Long, list: List<BigDecimal>, currency: String): MonthAndTotal {
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
    }
}