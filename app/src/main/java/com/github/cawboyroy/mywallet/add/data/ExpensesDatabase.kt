package com.github.cawboyroy.mywallet.add.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {

    abstract fun dao(): ExpensesDao
}