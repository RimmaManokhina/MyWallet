package com.github.cawboyroy.mywallet.chart.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.cawboyroy.mywallet.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.math.BigDecimal

@Composable
fun ChartScreenUi(
    navController: NavController,
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    isExpenses: Boolean,
    changeIsExpenses: (Boolean) -> Unit,
    month: String,
    monthTotal: String,
    pieRecords: PersistentList<FinancialRecordChartUi>,
    onButtonClickInPieChart: (FinancialRecordChartUi) -> Unit,
    detailsList: PersistentList<FinancialRecordChartUi>,
    onHeaderClick: (Pair<String, Boolean>) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            AddFloatingActionButton(navController)
        }
    ) { contentPadding ->
        val onRecordClick: (Long) -> Unit = { recordId ->
            navController.navigate("edit/$recordId")
        }
        val listState = rememberLazyListState()
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            TopUi(
                onLeftButtonClick,
                onRightButtonClick,
                isExpenses,
                changeIsExpenses,
            )

            Row(
                modifier = Modifier.Companion.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {

                Text(
                    modifier = Modifier.Companion
                        .padding(all = 4.dp)
                        .testTag("ChartScreenMonth"),
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
                        .testTag("ChartScreenMonthTotal"),
                    text = monthTotal,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = LocalContentColor.current,
                        textAlign = TextAlign.Companion.Center
                    )
                )
            }
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp
            LazyColumn(
                state = listState,
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .testTag("ChartScreenContentLazyColumn")
            ) {
                item {
                    PieChartSegmentsWithIcons(
                        screenWidthDp,
                        modifier = Modifier.Companion
                            .testTag("PieChartInLazyColumn")
                            .fillMaxWidth()
                            .height(screenWidthDp.dp),
                        records = pieRecords,
                        onClick = onButtonClickInPieChart
                    )
                }

                itemsIndexed(
                    items = detailsList,
                    key = { index, item -> item.toString() }) { index, listItem ->
                    listItem.Show(
                        index,
                        Modifier.Companion.animateItem(),
                        onRecordClick,
                        onHeaderClick
                    )
                }

                item {
                    Spacer(modifier = Modifier.Companion.height(72.dp))
                }
            }
        }
    }
}

@Composable
fun TopUi(x0: () -> Unit, x1: () -> Unit, x2: Boolean, x3: (Boolean) -> Unit) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun PreviewChartScreenUi() {
    val percentages = listOf(
        40f, 30f, 20f, 10f
    )
    val segments = listOf(
        PieSegment(Color(0xFFEF5350), R.drawable.ic_category_gifts, "Gifts"),
        PieSegment(Color(0xFF42A5F5), R.drawable.ic_category_kids, "Kids"),
        PieSegment(Color(0xFFFFCA28), R.drawable.ic_checkbook, "Groceries"),
        PieSegment(Color(0xFF66BB6A), R.drawable.ic_category_taxes, "Other")
    )
    val list = segments.mapIndexed { index, it ->
        val categoryHeader: FinancialRecordChartUi.CategoryHeader =
            FinancialRecordChartUi.CategoryHeader(
                true,
                "$",
                it.label,
                BigDecimal.ZERO.toString(),
                true,
                percentages[index]
            )
        categoryHeader
    }.toPersistentList()
    ChartScreenUi(
        rememberNavController(),
        {},
        {},
        true,
        {

        },
        "August",
        "$ 1,000",
        list,
        {},
        list,
        {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewChartScreenUiEmpty() {
    ChartScreenUi(
        rememberNavController(),
        {},
        {},
        true,
        {

        },
        "August",
        "$ 1,000",
        persistentListOf(),
        {},
        persistentListOf(),
        {}
    )
}