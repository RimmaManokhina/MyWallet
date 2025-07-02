package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ListContent(paddingValues: PaddingValues) { //todo use padding
    val viewModel: ListViewModel = hiltViewModel()
    val data = viewModel.state.collectAsStateWithLifecycle(emptyList()).value

    LazyColumn {
        items(data.size) {
            val record: ExpenseRecord = data[it]
            ExpenseRecordUi(record)
        }
    }
}