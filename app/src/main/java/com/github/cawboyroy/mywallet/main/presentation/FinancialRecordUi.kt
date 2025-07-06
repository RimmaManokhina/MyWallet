package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import java.math.BigDecimal

interface FinancialRecordUi {

    @Composable
    fun Show(actions: RecordActions, onClick: (Long) -> Unit)

    fun id(): String

    fun sum(): BigDecimal

    data class DayCollapsed(
        private val date: String,
        private val day: Int,
        private val sum: BigDecimal,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(actions: RecordActions, onClick: (Long) -> Unit) = DayUi(
            R.string.expand,
            Icons.Filled.KeyboardArrowUp,
            date,
            HandleMoney.formatWhole(sum.toString())
        ) {
            actions.expand(day)
        }

        override fun sum() = sum

        override fun id() = date + sum
    }

    data class DayExpanded(
        private val date: String,
        private val day: Int,
        private val sum: BigDecimal,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(actions: RecordActions, onClick: (Long) -> Unit) = DayUi(
            R.string.collapse,
            Icons.Filled.KeyboardArrowDown,
            date,
            HandleMoney.formatWhole(sum.toString())
        ) {
            actions.collapse(day)
        }

        override fun sum() = sum

        override fun id() = date + sum
    }

    data class Base(
        val isExpenses: Boolean,
        val money: String,
        private val title: String,
        private val category: String,
        private val id: Long,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(actions: RecordActions, onClick: (Long) -> Unit) {
            FinancialRecordInListUi(
                id = id,
                isExpenses = isExpenses,
                money = HandleMoney.formatWhole(money),
                title = title,
                category = category,
                onClick = onClick
            )
        }

        override fun id() = id.toString()

        override fun sum(): BigDecimal = BigDecimal.ZERO
    }
}

interface RecordActions {

    fun collapse(id: Int)

    fun expand(id: Int)
}