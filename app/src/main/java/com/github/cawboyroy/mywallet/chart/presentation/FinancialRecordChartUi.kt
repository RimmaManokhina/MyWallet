package com.github.cawboyroy.mywallet.chart.presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import com.github.cawboyroy.mywallet.main.presentation.categoryResId
import com.github.cawboyroy.mywallet.main.presentation.drawableResId
import com.github.cawboyroy.mywallet.main.presentation.findCategory
import java.math.BigDecimal

interface FinancialRecordChartUi {

    @Composable
    fun Show(
        index: Int,
        modifier: Modifier,
        onRecordClick: (Long) -> Unit,
        onHeaderClick: (Pair<String, Boolean>) -> Unit
    )

    fun sum(): BigDecimal

    fun percentage(): Float

    fun pieSegment(context: Context): PieSegment

    fun category(): String

    data class CategoryHeader(
        private val collapsed: Boolean,
        private val currency: String,
        private val category: String,
        private val money: String,
        private val isExpenses: Boolean,
        private val percentage: Float,
    ) : FinancialRecordChartUi {
        override fun category() = category

        @Composable
        override fun Show(
            index: Int,
            modifier: Modifier,
            onRecordClick: (Long) -> Unit,
            onHeaderClick: (Pair<String, Boolean>) -> Unit
        ) {
            val context = LocalContext.current
            val iconResId = context.categoryResId(category, isExpenses)
            val details = toString()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .testTag("ChartScreenHeader at $index")
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                val iconSize = 48.dp
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(iconSize)
                        .background(
                            color = colorResource(R.color.white),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Icon(
                        painter = painterResource(iconResId),
                        contentDescription = details,
                        modifier = Modifier.Companion
                            .size(iconSize * 0.6f)
                            .testTag("CategoryHeaderIcon at $index")
                            .semantics {
                                this.drawableResId = iconResId
                            },
                        tint = Color.Companion.Black
                    )
                }
                Text(
                    text = details,
                    modifier = Modifier
                        .testTag("CategoryHeaderDetails at $index")
                        .weight(1f)
                        .padding(16.dp),
                    textAlign = TextAlign.Companion.Start
                )
                Button(
                    modifier = Modifier.testTag("CategoryHeaderCollapsableIcon at $index"),
                    onClick = {
                        onHeaderClick.invoke(Pair(category, collapsed))
                    }
                ) {
                    Icon(
                        if (collapsed) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(if (collapsed) R.string.expand else R.string.collapse)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        override fun sum() = BigDecimal(money)

        override fun percentage() = percentage

        override fun pieSegment(context: Context): PieSegment {
            val categoryIcon = context.findCategory(category, isExpenses)
            return PieSegment(
                color = categoryIcon.color,
                imageVectorResId = categoryIcon.iconResId,
                label = category
            )
        }

        override fun toString(): String {
            return "$category ${"%.2f".format(percentage)}%\n" +
                    HandleMoney.formatWhole(currency, money)
        }
    }

    data class RecordDetail(
        private val currency: String,
        private val isExpenses: Boolean,
        private val money: String,
        private val title: String,
        private val category: String,
        private val id: Long,
        private val timeUi: String
    ) : FinancialRecordChartUi {

        override fun percentage() = 0f

        @Composable
        override fun Show(
            index: Int,
            modifier: Modifier,
            onRecordClick: (Long) -> Unit,
            onHeaderClick: (Pair<String, Boolean>) -> Unit
        ) {
            val context = LocalContext.current
            FinancialRecordInListWithTimeUi(
                index,
                modifier,
                categoryId = context.categoryResId(category, isExpenses),
                id = id,
                isExpenses = isExpenses,
                money = HandleMoney.formatWhole(currency, money),
                title = title,
                category = category,
                time = timeUi,
                onClick = onRecordClick
            )
        }

        override fun sum() = BigDecimal(money)

        override fun pieSegment(context: Context) = throw IllegalStateException("not supported")

        override fun category() = category
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHeaderCollapsed() {
    FinancialRecordChartUi.CategoryHeader(
        true,
        "$",
        "Groceries",
        "1000",
        true,
        100f
    ).Show(0, Modifier, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PreviewHeaderExpanded() {
    FinancialRecordChartUi.CategoryHeader(
        false,
        "$",
        "Groceries",
        "1000",
        true,
        100f
    ).Show(0, Modifier, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PreviewDetails() {
    FinancialRecordChartUi.RecordDetail(
        "$",
        true,
        "1000",
        "bread",
        "Groceries",
        100L,
        "August 3 18:00"
    ).Show(0, Modifier, {}, {})
}
