package com.github.cawboyroy.mywallet.chart.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ChartScreen(navController: NavController) {
    val viewModel: ChartsViewModel = hiltViewModel()
    val records = viewModel.recordsFlow.collectAsStateWithLifecycle(persistentListOf()).value
    val screenState = viewModel.screenStateFlow.collectAsStateWithLifecycle().value
    val categoryDetails = rememberSaveable { mutableStateOf("") }

    val chartDetailsViewModel = hiltViewModel<ChartDetailsViewModel>()
    LaunchedEffect(records, screenState, categoryDetails.value) {
        chartDetailsViewModel.load(screenState, records, categoryDetails.value)
    }
    val detailsList = chartDetailsViewModel.recordsFlow.collectAsStateWithLifecycle().value
    val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
    val monthAndTotal = screenState.monthNameAndSum(records, currency)

    val pieChartViewModel = hiltViewModel<PieChartViewModel>()
    val pieRecords = pieChartViewModel.recordsFlow.collectAsStateWithLifecycle().value
    LaunchedEffect(records, screenState) {
        pieChartViewModel.convert(screenState, records)
    }

    ChartScreenUi(
        navController,
        viewModel::showPreviousMonth,
        viewModel::showNextMonth,
        screenState.isExpenses,
        viewModel::switch,
        monthAndTotal.month,
        monthAndTotal.total,
        pieRecords,
        { financialRecordChartUi ->
            categoryDetails.value = financialRecordChartUi.category()
        },
        detailsList,
        { (clickedCategory, collapsed) ->
            chartDetailsViewModel.changeDetails(
                records,
                clickedCategory,
                categoryDetails.value,
                collapsed,
                screenState
            )
        }
    )
}