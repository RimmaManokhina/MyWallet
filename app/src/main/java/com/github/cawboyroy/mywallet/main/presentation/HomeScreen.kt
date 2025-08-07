package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation.NavController
import kotlinx.collections.immutable.toPersistentList

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: ListViewModel = hiltViewModel()
    val records = viewModel.recordsFlow.collectAsStateWithLifecycle(emptyList()).value
    val state = viewModel.screenStateFlow.collectAsStateWithLifecycle().value
    val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
    val monthAndTotal = state.monthNameAndSum(records, currency)

    HomeScreenUi(
        navController = navController,
        isExpenses = state.isExpenses,
        month = monthAndTotal.month,
        monthTotal = monthAndTotal.total,
        allCollapsedUi = state.allCollapsed,
        recordsUi = records.toPersistentList(),

        changeIsExpenses = viewModel::switch,
        onLeftButtonClick = viewModel::showPreviousMonth,
        onRightButtonClick = viewModel::showNextMonth,
        allDayActions = viewModel,
        recordActions = viewModel
    )
}