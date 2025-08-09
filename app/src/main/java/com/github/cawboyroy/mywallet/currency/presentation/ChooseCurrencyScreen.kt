package com.github.cawboyroy.mywallet.currency.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.SaveButton
import com.github.cawboyroy.mywallet.add.presentation.Title
import java.util.Currency
import java.util.Locale

@Composable
fun ChooseCurrencyScreen(navController: NavController) {
    val viewModel = hiltViewModel<ChooseCurrencyViewModel>()
    val list: List<Pair<Currency, String>> =
        viewModel.state.collectAsStateWithLifecycle().value

    val close = viewModel.close.collectAsStateWithLifecycle().value
    close.Show(navController)

    val alreadyChosenCurrency = viewModel.chosenCurrency().collectAsStateWithLifecycle("").value

    ChooseCurrencyScreenUi(
        alreadyChosenCurrency, list,
        viewModel::save,
        viewModel::find
    )
}

@Composable
private fun ChooseCurrencyScreenUi(
    alreadyChosenCurrency: String,
    list: List<Pair<Currency, String>>,
    save: (String) -> Unit,
    find: (String) -> Unit
) {
    var input by rememberSaveable { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            SaveButton(enabled = input.trim().isNotEmpty()) {
                save(input)
            }
            Title(R.string.currency)
            if (alreadyChosenCurrency.isNotEmpty())
                Text(
                    text = alreadyChosenCurrency,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            BasicTextField(
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
                value = TextFieldValue(input, selection = TextRange(input.length)),
                onValueChange = {
                    input = it.text
                    find(input.trim())
                },
                modifier = Modifier
                    .testTag("ChooseCurrencyScreenChooseCurrencyInput")
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray),
                singleLine = true,
                decorationBox = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        it()
                    }
                }
            )
            list.forEach { currency ->
                Button(
                    modifier = Modifier
                        .testTag("CurrencyTag${currency.second}")
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        input = currency.first.getSymbol(Locale.getDefault())
                    }
                ) {
                    Text(text = currency.second)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChooseCurrencyScreenUi() {
    ChooseCurrencyScreenUi(
        "$", listOf(
            Pair(Currency.getAvailableCurrencies().first(), "Russian Ruble")
        ), {

        }, {

        })
}