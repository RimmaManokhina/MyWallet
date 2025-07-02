package com.github.cawboyroy.mywallet.add.data

import javax.inject.Inject

interface AddRepository {

    suspend fun add(text: String)

    class Base @Inject constructor(
        private val dao: ExpensesDao,
    ) : AddRepository {

        override suspend fun add(text: String) {
            dao.add(ExpenseEntity(System.currentTimeMillis(), text))
        }
    }
}