package com.github.cawboyroy.mywallet.main.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_records")
data class FinancialRecordEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("money")
    val money: Double,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("category")
    val category: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("time")
    val time: Long,
    @ColumnInfo("isExpenses")
    val isExpenses: Boolean,
)