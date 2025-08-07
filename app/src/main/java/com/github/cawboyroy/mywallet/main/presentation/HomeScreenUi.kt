package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.cawboyroy.mywallet.R
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
            FloatingActionButton(
                modifier = Modifier.Companion.testTag("HomeAddButton"),
                onClick = { navController.navigate("add") }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
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