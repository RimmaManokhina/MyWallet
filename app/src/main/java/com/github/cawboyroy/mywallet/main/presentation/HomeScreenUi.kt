package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.cawboyroy.mywallet.chart.presentation.AddFloatingActionButton
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun HomeScreenUi(
    navController: NavController,
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
    Scaffold(
        floatingActionButton = {
            AddFloatingActionButton(navController)
        }
    ) { contentPadding ->
        ListContentUi(
            contentPadding,
            { recordId ->
                navController.navigate("edit/$recordId")
            },
            onLeftButtonClick,
            onRightButtonClick,
            isExpenses,
            changeIsExpenses,
            month,
            monthTotal,
            allCollapsedUi,
            allDayActions,
            recordsUi,
            recordActions,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreenUi(
        rememberNavController(),
        onLeftButtonClick = {},
        onRightButtonClick = {},
        isExpenses = true,
        changeIsExpenses = {},
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