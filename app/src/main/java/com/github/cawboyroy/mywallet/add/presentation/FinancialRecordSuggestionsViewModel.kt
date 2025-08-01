package com.github.cawboyroy.mywallet.add.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.data.FinancialRecordSuggestionsRepository
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.main.presentation.categoryResId
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FinancialRecordSuggestionsViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val runAsync: RunAsync,
    private val repository: FinancialRecordSuggestionsRepository,
) : ViewModel() {

    private val innerState: MutableStateFlow<List<FinancialRecordSuggestionUi>> =
        MutableStateFlow(emptyList())
    val state: StateFlow<List<FinancialRecordSuggestionUi>>
        get() = innerState

    fun find(userInput: String, isExpenses: Boolean) {
        runAsync.runAsync(viewModelScope, {
            repository.find(userInput, isExpenses).map {
                FinancialRecordSuggestionUi.Base(
                    title = it.first,
                    category = it.second,
                    categoryIconResId = context.categoryResId(it.second, isExpenses)
                )
            }
        }) {
            innerState.value = it
        }
    }
}