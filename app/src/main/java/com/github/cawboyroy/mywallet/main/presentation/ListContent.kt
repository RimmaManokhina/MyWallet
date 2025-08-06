package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle

@Composable
fun ListContent(paddingValues: PaddingValues, onRecordClick: (Long) -> Unit) {
    val viewModel: ListViewModel = hiltViewModel()
    val records = viewModel.recordsFlow.collectAsStateWithLifecycle(emptyList()).value
    val state = viewModel.screenStateFlow.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.padding(paddingValues)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = viewModel::showPreviousMonth,
                modifier = Modifier
                    .padding(4.dp)
                    .testTag("LeftButton")
            ) {

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

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
            val monthAndTotal = state.monthNameAndSum(records, currency)
            Text(
                modifier = Modifier
                    .padding(all = 4.dp)
                    .testTag("ListContentMonth"),
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
                    .testTag("ListContentMonthTotal"),
                text = monthAndTotal.total,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = LocalContentColor.current,
                    textAlign = TextAlign.Center
                )
            )

            state.allCollapsed.Show(viewModel)
        }

        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            itemsIndexed(items = records, key = { i, item -> item.id() }) { index, it ->
                it.Show(index, Modifier.animateItem(), viewModel, onRecordClick)
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}