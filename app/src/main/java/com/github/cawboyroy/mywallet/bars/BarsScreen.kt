package com.github.cawboyroy.mywallet.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
fun BarsScreen() {
    val viewModel = hiltViewModel<BarsViewModel>()
    val records = viewModel.recordsFlow.collectAsStateWithLifecycle().value
    val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
    val state = viewModel.screenStateFlow.collectAsStateWithLifecycle().value

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = viewModel::showPreviousYear, modifier = Modifier.padding(4.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.previous_year)
                    )
                }

                AnimatedIncomeExpenseToggle(
                    Modifier.weight(1f),
                    if (state.isExpenses) 0 else 1
                ) {
                    viewModel.switch(it == 0)
                }

                Button(onClick = viewModel::showNextYear, modifier = Modifier.padding(4.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.next_year)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value
                val yearAndTotal = state.yearAndSum(records, currency)
                Text(
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .testTag("BarsYearTotal"),
                    text = yearAndTotal.year,
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
                        .testTag("BarsYear"),
                    text = yearAndTotal.total,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = LocalContentColor.current,
                        textAlign = TextAlign.Center
                    )
                )
            }

            val lazyListState: LazyListState = rememberLazyListState()
            var lazyRowHeightDp by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            LazyRow(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        lazyRowHeightDp = with(density) { coordinates.size.height.toDp() }
                    },
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = records,
                    key = { index, item -> item.toString() }) { index, it ->
                    Column(
                        Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxHeight()
                    ) {

                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(color = Color(if (state.isExpenses) 0xFFFF0000 else 0xFF008000))
                                .width(48.dp)
                                .height((lazyRowHeightDp * 0.8f) * it.percentage)
                        )
                        Text(
                            text = it.ui(currency),
                            maxLines = 2,
                            modifier = Modifier.testTag("BarText at $index")
                        )
                    }
                }
            }
        }
    }
}