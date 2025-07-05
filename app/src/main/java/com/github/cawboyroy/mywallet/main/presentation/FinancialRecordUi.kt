package com.github.cawboyroy.mywallet.main.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

@Composable
private fun DayUi(
    @StringRes description: Int,
    icon: ImageVector,
    date: String,
    sum: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = date, modifier = Modifier.weight(1f))
        Text(text = sum, modifier = Modifier.padding(horizontal = 4.dp))
        Button(onClick = onClick) {
            Icon(
                icon,
                contentDescription = stringResource(description)
            )
        }
    }
}