package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ListContent(paddingValues: PaddingValues) {
    val viewModel: ListViewModel = hiltViewModel()
    val data = viewModel.state.collectAsStateWithLifecycle(emptyList()).value
    val isExpenses = viewModel.isExpensesState.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.padding(paddingValues)) {
        AnimatedIncomeExpenseToggle(
            Modifier.padding(all = 8.dp),
            if (isExpenses) 0 else 1
        ) {
            viewModel.update(it == 0)
        }

        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            items(items = data, key = { item -> item.id }) {
                FinancialRecordUi(it)
            }
        }
    }
}