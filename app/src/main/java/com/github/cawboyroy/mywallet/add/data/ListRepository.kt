package com.github.cawboyroy.mywallet.add.data

import com.github.cawboyroy.mywallet.add.presentation.ExpenseRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ListRepository {

    fun list(): Flow<List<ExpenseRecord>>

    class Base @Inject constructor(
        private val dao: ExpensesDao,
    ) : ListRepository {

        override fun list(): Flow<List<ExpenseRecord>> {
            return dao.expenses().map { list ->
                list.map {
                    ExpenseRecord(
                        it.money,
                        it.title,
                        it.category,
                        it.description,
                        it.time,
                        it.id
                    )
                }
            }
        }
    }
}