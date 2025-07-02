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

    fun add(money: String, title: String, category: String, description: String, time: Long) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                addRepository.add(ExpenseRecord(money, title, category, description, time))
            }
        ) {
        }
    }

    fun canSave(
        money: String,
        title: String,
        category: String,
    ): Boolean {
        return money.isNotEmpty() && title.isNotEmpty() && category.isNotEmpty()
    }
}

data class ExpenseRecord(
    val money: String,
    val title: String,
    val category: String,
    val description: String,
    val time: Long,
    val id: Long = System.currentTimeMillis(),
)