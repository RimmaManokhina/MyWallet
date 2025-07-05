package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.easycode.simplywallet.R

@Composable
fun EditFinancialRecord(
    id: Long,
    onDismiss: () -> Unit,
) {
    val viewModel: EditFinancialRecordViewModel = hiltViewModel()
    LaunchedEffect(id) {
        viewModel.loadRecord(id)
    }
    val state = viewModel.recordState.collectAsStateWithLifecycle().value
    if (state.id != 0L)
        EditFinancialRecordInner(viewModel, state, onDismiss)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFinancialRecordInner(
    viewModel: EditFinancialRecordViewModel,
    state: FinancialRecord,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = viewModel::dismiss,
        sheetState = sheetState
    ) {
        var selectedIndex by rememberSaveable { mutableIntStateOf(if (state.isExpenses) 0 else 1) }
        AnimatedIncomeExpenseToggle(
            Modifier.padding(horizontal = 16.dp),
            selectedIndex
        ) { selectedIndex = it }
        var money by rememberSaveable { mutableStateOf(state.money.toString()) }
        MoneyField(money) { money = it }
        var title by rememberSaveable { mutableStateOf(state.title) }
        TitleField(title) { title = it }
        var category by rememberSaveable { mutableStateOf(state.category) }
        CategoryField(category) { category = it }
        var time by rememberSaveable { mutableLongStateOf(state.time) }
        ChooseTime(time) { time = it }
        var description by rememberSaveable { mutableStateOf(state.description) }
        DescriptionField(description) { description = it }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                modifier = Modifier.padding(start = 16.dp),
                onClick = {
                    viewModel.delete(state.id)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) onDismiss()
                    }
                }) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete))
            }
            SaveButton(
                viewModel.canSave(
                    selectedIndex == 0,
                    money,
                    title,
                    category,
                    time,
                    description
                )
            ) {
                viewModel.edit(selectedIndex == 0, money, title, category, time, description)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) onDismiss()
                }
            }
        }
    }
}