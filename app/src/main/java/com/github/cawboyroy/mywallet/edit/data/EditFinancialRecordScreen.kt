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
    val scrollState = rememberScrollState()

    val close = viewModel.close.collectAsStateWithLifecycle().value
    close.Show(navController)

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            var selectedIndex by rememberSaveable { mutableIntStateOf(if (record.isExpenses) 0 else 1) }
            AnimatedIncomeExpenseToggle(
                Modifier.padding(horizontal = 16.dp),
                selectedIndex
            ) { selectedIndex = it }
            var money by rememberSaveable { mutableStateOf(record.money) }
            MoneyField(true, money) { money = it }

            var title by rememberSaveable { mutableStateOf(record.title) }
            TitleField(title) { title = it }

            var category by rememberSaveable { mutableStateOf(record.category) }
            CategoryField(category) { category = it }

            var time by rememberSaveable { mutableLongStateOf(record.time) }
            ChooseTime(time) { time = it }

            var description by rememberSaveable { mutableStateOf(record.description) }
            DescriptionField(description) { description = it }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
            Row {
                DeleteButton {
                    viewModel.delete(record.id)
                }
                SaveButton(
                    viewModel.canSave(
                        record,
                        selectedIndex == 0,
                        money,
                        title,
                        category,
                        time,
                        description,
                        record.id
                    )
                ) {
                    viewModel.edit(
                        record.id,
                        selectedIndex == 0,
                        money,
                        title,
                        category,
                        time,
                        description
                    )
                }
            }
        }
    }
}