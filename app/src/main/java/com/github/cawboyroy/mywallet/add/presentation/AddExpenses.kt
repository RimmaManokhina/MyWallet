package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.cawboyroy.mywallet.add.data.SaveButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenses(
    viewModel: AddExpensesViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        var money by rememberSaveable { mutableStateOf("") }
        MoneyField(money) { money = it }
        var title by rememberSaveable { mutableStateOf("") }
        TitleField(title) { title = it }
        var category by rememberSaveable { mutableStateOf("") }
        CategoryField(category) { category = it }
        var time by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
        ChooseTime(time) { time = it }
        var description by rememberSaveable { mutableStateOf("") }
        DescriptionField(description) { description = it }

        SaveButton(viewModel.canSave(money, title, category)) {
            viewModel.add(money, title, category, description, time)
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) onDismiss()
            }
        }
    }
}