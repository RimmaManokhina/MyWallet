package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.layout.Column
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
import com.github.cawboyroy.mywallet.currency.presentation.ChooseCurrencyViewModel
import java.math.BigDecimal

@Composable
fun AddFinancialRecordScreen(
    navController: NavController,
    viewModel: AddFinancialRecordViewModel = hiltViewModel(),
) {

    val close by viewModel.close.collectAsStateWithLifecycle()
    close.Show(navController)
    val currencyViewModel = hiltViewModel<ChooseCurrencyViewModel>()
    val currency = currencyViewModel.chosenCurrency().collectAsStateWithLifecycle("").value

    AddFinancialRecordScreenUi(
        currency,
        viewModel.now(),
        viewModel::canSave,
        viewModel::add
    )
}

@Composable
fun AddFinancialRecordScreenUi(
    currency: String,
    now: Long,
    canSave: (String, String, String) -> Boolean,
    add: (String, String, String, String, Long, Boolean) -> Unit
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
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
            AnimatedIncomeExpenseToggle(
                Modifier.padding(horizontal = 16.dp),
                selectedIndex
            ) { selectedIndex = it }
            var money by rememberSaveable { mutableStateOf(BigDecimal.ZERO.toString()) }
            MoneyField(currency, false, money) { money = it }
            var title by rememberSaveable { mutableStateOf("") }
            var category by rememberSaveable { mutableStateOf("") }
            val onCategoryChange: (String) -> Unit = { category = it }
            val isExpenses: Boolean = selectedIndex == 0
            TitleField(title, isExpenses, onCategoryChange) { title = it }
            CategoryField(category, isExpenses, onCategoryChange)
            var time by rememberSaveable { mutableLongStateOf(now) }
            ChooseTime(time) { time = it }
            var description by rememberSaveable { mutableStateOf("") }
            DescriptionField(description) { description = it }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
            SaveButton(canSave(money, title, category)) {
                add(money, title, category, description, time, isExpenses)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddScreen() {
    AddFinancialRecordScreenUi(
        "$", System.currentTimeMillis(),
        { a, b, c -> true }
    ) { a, b, c, d, e, f ->
    }
}