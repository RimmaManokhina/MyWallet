package com.github.cawboyroy.mywallet.main.presentation

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.CategoryIcon
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import com.github.cawboyroy.mywallet.add.presentation.expensesCategoryList
import com.github.cawboyroy.mywallet.add.presentation.incomeCategoryList
import java.math.BigDecimal

interface FinancialRecordUi {

    @Composable
    fun Show(index: Int, modifier: Modifier, actions: RecordActions, onClick: (Long) -> Unit)

    fun id(): String

    fun sum(): BigDecimal

    data class DayCollapsed(
        private val currency: String,
        private val date: String,
        private val day: Int,
        private val sum: BigDecimal,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(
            index: Int,
            modifier: Modifier,
            actions: RecordActions,
            onClick: (Long) -> Unit
        ) =
            DayUi(
                index,
                modifier,
                R.string.expand,
                Icons.Filled.KeyboardArrowDown,
                date,
                HandleMoney.formatWhole(currency, sum.toString())
            ) {
                actions.expand(day)
            }

        override fun sum() = sum

        override fun id() = date + sum
    }

    data class DayExpanded(
        private val currency: String,
        private val date: String,
        private val day: Int,
        private val sum: BigDecimal,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(
            index: Int,
            modifier: Modifier,
            actions: RecordActions,
            onClick: (Long) -> Unit
        ) =
            DayUi(
                index,
                modifier,
                R.string.collapse,
                Icons.Filled.KeyboardArrowUp,
                date,
                HandleMoney.formatWhole(currency, sum.toString())
            ) {
                actions.collapse(day)
            }

        override fun sum() = sum

        override fun id() = date + sum
    }

    data class Base(
        private val currency: String,
        private val isExpenses: Boolean,
        private val money: String,
        private val title: String,
        private val category: String,
        private val id: Long,
    ) : FinancialRecordUi {

        @Composable
        override fun Show(
            index: Int,
            modifier: Modifier,
            actions: RecordActions,
            onClick: (Long) -> Unit
        ) {
            val context = LocalContext.current
            FinancialRecordInListUi(
                index,
                modifier,
                categoryId = context.categoryResId(category, isExpenses),
                id = id,
                isExpenses = isExpenses,
                money = HandleMoney.formatWhole(currency, money),
                title = title,
                category = category,
                onClick = onClick
            )
        }

        override fun id() = id.toString()

        override fun sum(): BigDecimal = BigDecimal.ZERO
    }
}

@DrawableRes
fun Context.categoryResId(category: String, isExpenses: Boolean) =
    findCategory(category, isExpenses).iconResId

fun Context.findCategory(category: String, isExpenses: Boolean): CategoryIcon {
    val list = if (isExpenses) expensesCategoryList else incomeCategoryList
    return (list.find {
        getString(it.titleResId).equals(category, ignoreCase = true)
    } ?: list.last())
}

interface RecordActions {

    fun collapse(id: Int)

    fun expand(id: Int)
}