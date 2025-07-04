package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.main.data.AddRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddFinancialRecordViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val addRepository: AddRepository,
) : ViewModel() {

    fun add(
        money: String,
        title: String,
        category: String,
        description: String,
        time: Long,
        isExpenses: Boolean,
    ) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = {
                addRepository.add(
                    FinancialRecord(
                        money.toDouble(),
                        title,
                        category,
                        description,
                        time,
                        isExpenses
                    )
                )
            }
        ) {
        }
    }
}

data class FinancialRecord(
    val money: Double,
    val title: String,
    val category: String,
    val description: String,
    val time: Long,
    val isExpenses: Boolean,
    val id: Long = System.currentTimeMillis(),
)