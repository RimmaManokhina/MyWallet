package com.github.cawboyroy.mywallet.add.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ListRepository {

    fun list(): Flow<List<String>>

    class Base @Inject constructor(
        private val dao: ExpensesDao
    ) : ListRepository {

        override fun list(): Flow<List<String>> {
            return dao.expenses().map { list ->
                list.map { it.title }
            }
        }
    }
}