package com.github.cawboyroy.mywallet.main.data


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialRecordsDao {

    @Upsert
    suspend fun add(financialRecordEntity: FinancialRecordEntity)

    @Query("SELECT * FROM financial_records WHERE isExpenses=:isExpenses AND time < :max AND time >= :min ORDER BY time DESC")

    fun financialRecords(
        isExpenses: Boolean,
        min: Long,
        max: Long
    ): Flow<List<FinancialRecordEntity>>
}