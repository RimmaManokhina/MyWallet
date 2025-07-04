package com.github.cawboyroy.mywallet.add.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FinancialRecordEntity::class], version = 1)
abstract class FinancialRecordsDatabase : RoomDatabase() {

    abstract fun dao(): FinancialRecordsDao
}