package com.github.cawboyroy.mywallet.add.data

import com.github.cawboyroy.mywallet.add.presentation.ExpenseRecord
import javax.inject.Inject

interface AddRepository {

    suspend fun add(record: ExpenseRecord)

    class Base @Inject constructor(
        private val dao: ExpensesDao,
    ) : AddRepository {

        override suspend fun add(record: ExpenseRecord) {
            dao.add(
                ExpenseEntity(
                    id = record.id,
                    money = record.money,
                    title = record.title,
                    category = record.category,
                    description = record.description,
                    time = record.time
                )
            )
        }
    }
}