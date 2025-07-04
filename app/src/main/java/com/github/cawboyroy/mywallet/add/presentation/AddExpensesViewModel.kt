package com.github.cawboyroy.mywallet.add.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.data.AddRepository
import com.github.cawboyroy.mywallet.core.RunAsync
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
                        money,
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
    val money: String,
    val title: String,
    val category: String,
    val description: String,
    val time: Long,
    val isExpenses: Boolean,
    val id: Long = System.currentTimeMillis(),
)