package com.github.cawboyroy.mywallet.edit.data

import com.github.cawboyroy.mywallet.main.data.FinancialRecordEntity
import com.github.cawboyroy.mywallet.main.data.FinancialRecordsDao
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import javax.inject.Inject

interface EditRepository {

    suspend fun record(id: Long): FinancialRecord

    suspend fun edit(record: FinancialRecord)

    suspend fun delete(id: Long)

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : EditRepository {

        override suspend fun record(id: Long): FinancialRecord {
            val record = dao.record(id)
            return FinancialRecord(
                record.money,
                record.title,
                record.category,
                record.description,
                record.time,
                record.isExpenses,
                record.id
            )
        }

        override suspend fun edit(record: FinancialRecord) {
            dao.add(
                FinancialRecordEntity(
                    record.id,
                    record.money,
                    record.title,
                    record.category,
                    record.description,
                    record.time,
                    record.isExpenses
                )
            )
        }

        override suspend fun delete(id: Long) {
            dao.delete(id)
        }
    }
}