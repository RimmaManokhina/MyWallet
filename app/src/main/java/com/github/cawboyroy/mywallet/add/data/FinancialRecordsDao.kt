package com.github.cawboyroy.mywallet.add.data


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialRecordsDao {

    @Upsert
    suspend fun add(financialRecordEntity: FinancialRecordEntity)

    @Query("SELECT * FROM financial_records WHERE isExpenses=:isExpenses")
    fun financialRecords(isExpenses: Boolean): Flow<List<FinancialRecordEntity>>
}