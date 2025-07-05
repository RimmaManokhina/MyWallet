package com.github.cawboyroy.mywallet.main.data

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ListRepository {

    fun list(isExpenses: Boolean, time: MonthsUi): Flow<List<FinancialRecord>>

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : ListRepository {

        override fun list(isExpenses: Boolean, time: MonthsUi): Flow<List<FinancialRecord>> {
            val (min, max) = time.monthBoundaries()


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