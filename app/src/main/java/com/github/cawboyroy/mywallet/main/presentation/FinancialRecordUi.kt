package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


interface FinancialRecordUi {

    @Composable
    fun Show()

    fun id(): String

    data class Day(private val date: String) : FinancialRecordUi {

        @Composable
        override fun Show() {
            Text(
                text = date, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
        override fun id() = date
    }

    data class Base(
        val money: Double,
        private val title: String,
        private val category: String,
        private val description: String,
        val time: Long,
        private val isExpenses: Boolean,
        private val id: Long = System.currentTimeMillis(),
    ) : FinancialRecordUi {

        @Composable
        override fun Show() {
            FinancialRecordInListUi(
                money = money,
                title = title,
                category = category,
            )
        }
        override fun id() = id.toString()
    }
}