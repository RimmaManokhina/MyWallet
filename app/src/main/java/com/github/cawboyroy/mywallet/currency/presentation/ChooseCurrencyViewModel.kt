package com.github.cawboyroy.mywallet.currency.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.presentation.Close
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChooseCurrencyViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val chosenCurrencyRepository: ChosenCurrencyRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val closeState: MutableStateFlow<Close> = MutableStateFlow(Close.Empty)
    val close: StateFlow<Close>
        get() = closeState

    private val allCurrencies = Currency.getAvailableCurrencies()

    fun chosenCurrency() = chosenCurrencyRepository.value()

    val state: StateFlow<List<Pair<Currency, String>>> =
        savedStateHandle.getStateFlow(CURRENCIES_KEY, emptyList())

    init {
        if (state.value.isEmpty()) runAsync.runAsync(viewModelScope, {
            allCurrencies.map { Pair(it, ui(it)) }.sortedBy { it.second }
        }) {
            savedStateHandle[CURRENCIES_KEY] = it
        }
    }

    fun find(input: String) = runAsync.runAsync(viewModelScope, {
        (if (input.isEmpty())
            allCurrencies
        else
            allCurrencies.filter { currency ->
                currency.displayName.startsWith(input, ignoreCase = true) ||
                        currency.symbol.startsWith(input, ignoreCase = true) ||
                        currency.currencyCode.startsWith(input, ignoreCase = true)
            }
                ).map {
                Pair(it, ui(it))
            }.sortedBy { it.second }
    }) {
        savedStateHandle[CURRENCIES_KEY] = it
    }

    private fun ui(currency: Currency): String {
        val symbol = currency.getSymbol(Locale.getDefault())//$
        val currencyCode = currency.currencyCode//USD
        val middlePart = if (symbol.equals(currencyCode, true))
            " "
        else
            " $currencyCode "
        return symbol + middlePart + currency.displayName //US DOLLAR
    }

    fun save(input: String) = runAsync.runAsync(viewModelScope, {
        chosenCurrencyRepository.save(input)
    }) {
        closeState.value = Close.Back
    }

    companion object {
        private const val CURRENCIES_KEY = "currency"
    }
}