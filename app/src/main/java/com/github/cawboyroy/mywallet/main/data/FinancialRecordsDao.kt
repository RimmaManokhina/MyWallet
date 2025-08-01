package com.github.cawboyroy.mywallet.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialRecordsDao {

    @Query("SELECT * FROM financial_records WHERE isExpenses=:isExpenses AND title LIKE :searchQuery || '%'")
    suspend fun financialRecordsStartingWith(
        searchQuery: String,
        isExpenses: Boolean
    ): List<FinancialRecordEntity>

    @Query("DELETE FROM financial_records")
    suspend fun clearAll()

    @Insert
    suspend fun addAll(list: List<FinancialRecordEntity>)

    @Query("SELECT * FROM financial_records")
    suspend fun all(): List<FinancialRecordEntity>

    @Upsert
    suspend fun add(financialRecordEntity: FinancialRecordEntity)

    @Query("SELECT * FROM financial_records WHERE isExpenses=:isExpenses AND time < :max AND time >= :min ORDER BY time ASC")
    fun financialRecords(
        isExpenses: Boolean,
        min: Long,
        max: Long
    ): Flow<List<FinancialRecordEntity>>

    @Query("DELETE FROM financial_records WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM financial_records WHERE id = :id")
    suspend fun record(id: Long): FinancialRecordEntity
}