package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import com.github.cawboyroy.mywallet.R

interface FinancialRecordUi {

    @Composable
    fun Show(actions: RecordActions)

    fun id(): String

    fun sum(): Double

    data class DayCollapsed(
        private val date: String,
        private val day: Int,
        private val sum: Double,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(actions: RecordActions) = DayUi(
            R.string.expand,
            Icons.Filled.KeyboardArrowUp,
            date,
            sum.toString()
        ) {
            actions.expand(day)
        }

        override fun sum() = sum

        override fun id() = date + sum
    }

    data class DayExpanded(
        private val date: String,
        private val day: Int,
        private val sum: Double,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(actions: RecordActions) = DayUi(
            R.string.collapse,
            Icons.Filled.KeyboardArrowDown,
            date,
            sum.toString()
        ) {
            actions.collapse(day)
        }

        override fun sum() = sum

        override fun id() = date + sum
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
        override fun Show(actions: RecordActions) {
            FinancialRecordInListUi(
                money = money,
                title = title,
                category = category,
            )
        }

        override fun id() = id.toString()

        override fun sum() = 0.0
    }
}

interface RecordActions {

    fun collapse(id: Int)

    fun expand(id: Int)
}