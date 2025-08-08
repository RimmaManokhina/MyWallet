package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.collections.immutable.PersistentList
import java.io.Serializable

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeScreenViewModel = hiltViewModel()
    val uiState = viewModel.screenState.collectAsStateWithLifecycle().value

    HomeScreenUi(
        navController = navController,
        isExpenses = uiState.isExpenses,
        month = uiState.month,
        monthTotal = uiState.total,
        allCollapsedUi = uiState.allCollapsedUi,
        recordsUi = uiState.recordsUi,

        changeIsExpenses = viewModel::switch,
        onLeftButtonClick = viewModel::showPreviousMonth,
        onRightButtonClick = viewModel::showNextMonth,
        allDayActions = viewModel,
        recordActions = viewModel
    )
}

data class HomeScreenState(
    val now: Long,
    val isExpenses: Boolean,
    val month: String,
    val total: String,
    val allCollapsedUi: AllCollapsedUi,
    val recordsUi: PersistentList<FinancialRecordUi>
) : Serializable