package com.github.cawboyroy.mywallet.add.data

import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.main.data.FinancialRecordEntity
import com.github.cawboyroy.mywallet.main.data.FinancialRecordsDao
import javax.inject.Inject

interface AddRepository {

    suspend fun add(record: FinancialRecord)

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : AddRepository {

        override suspend fun add(record: FinancialRecord) {
            dao.add(
                FinancialRecordEntity(
                    id = record.id,
                    money = record.money,
                    title = record.title,
                    category = record.category,
                    description = record.description,
                    time = record.time,
                    isExpenses = record.isExpenses
                )
            )
        }
    }
}