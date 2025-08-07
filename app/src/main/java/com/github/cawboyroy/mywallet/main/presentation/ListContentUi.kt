package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun ListContentUi(
    paddingValues: PaddingValues,
    onRecordClick: (Long) -> Unit,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    isExpenses: Boolean,
    changeIsExpenses: (Boolean) -> Unit,
    month: String,
    monthTotal: String,
    allCollapsedUi: AllCollapsedUi,
    allDayActions: AllDayActions,
    recordsUi: PersistentList<FinancialRecordUi>,
    recordActions: RecordActions,
) {
    Column(modifier = Modifier.Companion.padding(paddingValues)) {
        Row(verticalAlignment = Alignment.Companion.CenterVertically) {
            Button(
                onClick = onLeftButtonClick,
                modifier = Modifier.Companion
                    .padding(4.dp)
                    .testTag("LeftButton")
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.previous_month)
                )
            }

            AnimatedIncomeExpenseToggle(
                Modifier.Companion.weight(1f),
                if (isExpenses) 0 else 1
            ) {
                changeIsExpenses(it == 0)
            }

            Button(onClick = onRightButtonClick, modifier = Modifier.Companion.padding(4.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.next_month)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {

            Text(
                modifier = Modifier.Companion
                    .padding(all = 4.dp)
                    .testTag("ListContentMonth"),
                text = month,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = LocalContentColor.current,
                    textAlign = TextAlign.Companion.Center
                )
            )
            Spacer(modifier = Modifier.Companion.weight(1f))
            Text(
                modifier = Modifier.Companion
                    .padding(all = 4.dp)
                    .testTag("ListContentMonthTotal"),
                text = monthTotal,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = LocalContentColor.current,
                    textAlign = TextAlign.Companion.Center
                )
            )

            allCollapsedUi.Show(allDayActions)
        }

        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.Companion.fillMaxSize(),
            state = listState,
        ) {
            itemsIndexed(items = recordsUi, key = { i, item -> item.id() }) { index, it ->
                it.Show(index, Modifier.Companion.animateItem(), recordActions, onRecordClick)
            }
            item {
                Spacer(modifier = Modifier.Companion.height(72.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListContentUiExpanded() {
    val isExpenses = remember { mutableStateOf(true) }
    ListContentUi(
        paddingValues = PaddingValues(8.dp),
        onRecordClick = {},
        onLeftButtonClick = {},
        onRightButtonClick = {},
        isExpenses = isExpenses.value,
        changeIsExpenses = {
            isExpenses.value = it
        },
        month = "August",
        monthTotal = "$ 1,000",
        allCollapsedUi = AllCollapsedUi.Expanded,
        allDayActions = object : AllDayActions {
            override fun expandAll() = Unit
            override fun collapseAll() = Unit
        },
        recordsUi = persistentListOf(
            FinancialRecordUi.DayExpanded("$", "August 1", 1, BigDecimal(1000)),
            FinancialRecordUi.Base("$", true, "1000", "Food", "Groceries", 123)
        ),
        recordActions = object : RecordActions {
            override fun collapse(id: Int) = Unit
            override fun expand(id: Int) = Unit
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewListContentUiCollapsed() {
    val isExpenses = remember { mutableStateOf(false) }
    ListContentUi(
        paddingValues = PaddingValues(8.dp),
        onRecordClick = {},
        onLeftButtonClick = {},
        onRightButtonClick = {},
        isExpenses = isExpenses.value,
        changeIsExpenses = {
            isExpenses.value = it
        },
        month = "August",
        monthTotal = "$ 1,000",
        allCollapsedUi = AllCollapsedUi.Collapsed,
        allDayActions = object : AllDayActions {
            override fun expandAll() = Unit
            override fun collapseAll() = Unit
        },
        recordsUi = persistentListOf(
            FinancialRecordUi.DayCollapsed("$", "August 1", 1, BigDecimal(1000)),
        ),
        recordActions = object : RecordActions {
            override fun collapse(id: Int) = Unit
            override fun expand(id: Int) = Unit
        },
    )
}