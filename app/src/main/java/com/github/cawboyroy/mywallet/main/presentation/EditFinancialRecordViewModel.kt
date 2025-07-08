package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.main.data.EditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import javax.inject.Inject

@HiltViewModel
class EditFinancialRecordViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val repository: EditRepository,
) : ViewModel() {

    private val empty: FinancialRecord = FinancialRecord(0.0, "", "", "", 0L, true, 0)
    private val recordInnerState: MutableStateFlow<FinancialRecord> =
        MutableStateFlow<FinancialRecord>(empty)
    val recordState: StateFlow<FinancialRecord>
        get() = recordInnerState
    //todo make ui with empty initial state

    fun loadRecord(id: Long) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = { repository.record(id) }
        ) {
            recordInnerState.value = it
        }
    }

    fun canSave(
        isExpenses: Boolean,
        money: String,
        title: String,
        category: String,
        time: Long,
        description: String,
    ) = FinancialRecord(
        money.toDouble(),
        title,
        category,
        description,
        time,
        isExpenses,
        recordState.value.id
    ) != recordState.value

    fun delete(id: Long) {
        runAsync.runAsync(scope = viewModelScope, background = {
            repository.delete(id)
        }) {
            recordInnerState.value = empty
        }
    }

    fun edit(
        isExpenses: Boolean,
        money: String,
        title: String,
        category: String,
        time: Long,
        description: String,
    ) {
        runAsync.runAsync(scope = viewModelScope, background = {
            repository.edit(
                FinancialRecord(
                    money.toDouble(),
                    title,
                    category,
                    description,
                    time,
                    isExpenses,
                    recordState.value.id
                )
            )
        }) {
            recordInnerState.value = empty
        }
    }

    fun dismiss() {
        recordInnerState.value = empty
    }
}