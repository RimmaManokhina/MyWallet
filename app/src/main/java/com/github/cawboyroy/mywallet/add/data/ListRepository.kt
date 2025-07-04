package com.github.cawboyroy.mywallet.add.data

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

interface ListRepository {

    fun list(isExpenses: Boolean): Flow<List<FinancialRecord>>

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : ListRepository {

        override fun list(isExpenses: Boolean): Flow<List<FinancialRecord>> {
            return dao.financialRecords(isExpenses).map { list ->
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