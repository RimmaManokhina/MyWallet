package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle
import androidx.compose.material3.LocalContentColor

@Composable
fun ListContent(paddingValues: PaddingValues) {
    val viewModel: ListViewModel = hiltViewModel()
    val records = viewModel.recordsFlow.collectAsStateWithLifecycle(emptyList()).value
    val state = viewModel.screenStateFlow.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.padding(paddingValues)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = viewModel::showPreviousMonth, modifier = Modifier.padding(4.dp)) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.previous_month)
                )
            }

            AnimatedIncomeExpenseToggle(
                Modifier.weight(1f),
                if (state.isExpenses) 0 else 1
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
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            text = state.monthNameAndSum(records),
            style = TextStyle(
                fontSize = 18.sp,
                color = LocalContentColor.current,
                textAlign = TextAlign.Center
            )
        )

        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            items(items = records, key = { item -> item.id() }) {
                it.Show(viewModel)
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}