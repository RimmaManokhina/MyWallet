package com.github.cawboyroy.mywallet.add.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Upsert
    suspend fun add(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM expenses")
    fun expenses(): Flow<List<ExpenseEntity>>
}