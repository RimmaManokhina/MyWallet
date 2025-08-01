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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ChartScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { contentPadding -> //todo DRY ListContent
        val viewModel: ChartsViewModel = hiltViewModel()
        val records = viewModel.recordsFlow.collectAsStateWithLifecycle(persistentListOf()).value
        val screenState = viewModel.screenStateFlow.collectAsStateWithLifecycle().value
        val categoryDetails = rememberSaveable { mutableStateOf("") }
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp

        val chartDetailsViewModel = hiltViewModel<ChartDetailsViewModel>()
        LaunchedEffect(records, screenState, categoryDetails.value) {
            chartDetailsViewModel.load(screenState, records, categoryDetails.value)
        }
        val detailsList = chartDetailsViewModel.recordsFlow.collectAsStateWithLifecycle().value
        val onRecordClick: (Long) -> Unit = { recordId ->
            navController.navigate("edit/$recordId")
        }
        val listState = rememberLazyListState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = viewModel::showPreviousMonth,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.previous_month)
                    )
                }

                AnimatedIncomeExpenseToggle(
                    Modifier.weight(1f),
                    if (screenState.isExpenses) 0 else 1
                ) {
                    viewModel.switch(it == 0)
                }

                Button(onClick = viewModel::showNextMonth, modifier = Modifier.padding(4.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.next_month)
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
                val monthAndTotal = screenState.monthNameAndSum(records, currency)
                Text(
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .testTag("ChartScreenMonth"),
                    text = monthAndTotal.month,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = LocalContentColor.current,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .testTag("ChartScreenMonthTotal"),
                    text = monthAndTotal.total,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = LocalContentColor.current,
                        textAlign = TextAlign.Center
                    )
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    PieChartSegmentsWithIcons(
                        screenWidthDp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenWidthDp.dp),
                        screenState = screenState,
                        outerRecords = records,
                    ) {
                        categoryDetails.value = it.category()
                    }
                }
                itemsIndexed(
                    items = detailsList,
                    key = { index, item -> item.toString() }) { index, listItem ->
                    listItem.Show(
                        index,
                        Modifier.animateItem(),
                        onRecordClick
                    ) { (clickedCategory, collapsed) ->
                        chartDetailsViewModel.changeDetails(
                            records,
                            clickedCategory,
                            categoryDetails.value,
                            collapsed,
                            screenState
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}