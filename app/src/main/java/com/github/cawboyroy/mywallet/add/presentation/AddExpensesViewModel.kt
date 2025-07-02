package com.github.cawboyroy.mywallet.add.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.data.AddRepository
import com.github.cawboyroy.mywallet.core.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddExpensesViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val addRepository: AddRepository,
) : ViewModel() {

    fun add(text: String) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                addRepository.add(text)
            }
        ) {
        }
    }
}