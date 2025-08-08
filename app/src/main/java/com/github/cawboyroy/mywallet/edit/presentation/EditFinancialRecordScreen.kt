package com.github.cawboyroy.mywallet.edit.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle
import com.github.cawboyroy.mywallet.add.presentation.CategoryField
import com.github.cawboyroy.mywallet.add.presentation.ChooseTime
import com.github.cawboyroy.mywallet.add.presentation.DeleteButton
import com.github.cawboyroy.mywallet.add.presentation.DescriptionField
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.add.presentation.MoneyField
import com.github.cawboyroy.mywallet.add.presentation.SaveButton
import com.github.cawboyroy.mywallet.add.presentation.TitleField
import com.github.cawboyroy.mywallet.currency.presentation.ChooseCurrencyViewModel

@Composable
fun EditFinancialRecordScreen(
    id: Long,
    navController: NavController
) {
    val viewModel: EditFinancialRecordViewModel = hiltViewModel()
    val state: EditState = viewModel.state.collectAsStateWithLifecycle().value
    state.Show(id, viewModel, navController)
}

@Composable
fun EditFinancialRecordInner(
    viewModel: EditFinancialRecordViewModel,
    record: FinancialRecord,
    navController: NavController
) {
    val close = viewModel.close.collectAsStateWithLifecycle().value
    close.Show(navController)

    val currencyViewModel = hiltViewModel<ChooseCurrencyViewModel>()
    val currency = currencyViewModel.chosenCurrency().collectAsStateWithLifecycle("").value

    EditFinancialRecordInnerUi(
        record.isExpenses,
        currency,
        record.money,
        record.title,
        record.category,
        record.time,
        record.description,
        { viewModel.delete(record.id) },
        { isExpenses, money, title, category, time, description ->
            viewModel.canSave(
                record,
                isExpenses,
                money,
                title,
                category,
                time,
                description,
                record.id
            )
        }
    ) { isExpenses, money, title, category, time, description ->
        viewModel.edit(
            record.id,
            isExpenses,
            money,
            title,
            category,
            time,
            description
        )
    }
}

@Composable
fun EditFinancialRecordInnerUi(
    isExpenses: Boolean,
    currency: String,
    initialMoney: String,
    initialTitle: String,
    initialCategory: String,
    initialTime: Long,
    initialDescription: String,
    onDelete: () -> Unit,
    canSave: (Boolean, String, String, String, Long, String) -> Boolean,
    edit: (Boolean, String, String, String, Long, String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            var selectedIndex by rememberSaveable { mutableIntStateOf(if (isExpenses) 0 else 1) }
            AnimatedIncomeExpenseToggle(
                Modifier.padding(horizontal = 16.dp),
                selectedIndex
            ) { selectedIndex = it }
            var money by rememberSaveable { mutableStateOf(initialMoney) }
            MoneyField(currency, true, money) { money = it }
            var title by rememberSaveable { mutableStateOf(initialTitle) }
            var category by rememberSaveable { mutableStateOf(initialCategory) }
            val onCategoryChange: (String) -> Unit = { category = it }
            val isExpenses: Boolean = selectedIndex == 0
            TitleField(title, isExpenses, onCategoryChange) { title = it }
            CategoryField(category, isExpenses, onCategoryChange)
            var time by rememberSaveable { mutableLongStateOf(initialTime) }
            ChooseTime(time) { time = it }
            var description by rememberSaveable { mutableStateOf(initialDescription) }
            DescriptionField(description) { description = it }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
            Row {
                DeleteButton(onDelete)
                SaveButton(
                    canSave(
                        isExpenses,
                        money,
                        title,
                        category,
                        time,
                        description
                    )
                ) {
                    edit(isExpenses, money, title, category, time, description)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditFinancialRecordInnerUi() {
    EditFinancialRecordInnerUi(
        true,
        "$",
        "1000",
        "bread",
        "Groceries",
        System.currentTimeMillis(),
        "white one",
        {}, { isExpenses, money, title, category, time, description ->
            true
        }, { isExpenses, money, title, category, time, description ->
        }
    )
}