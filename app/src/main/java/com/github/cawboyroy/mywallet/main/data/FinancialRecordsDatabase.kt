package com.github.cawboyroy.mywallet.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FinancialRecordEntity::class], version = 1, exportSchema = false)

abstract class FinancialRecordsDatabase : RoomDatabase() {

    abstract fun dao(): FinancialRecordsDao
}