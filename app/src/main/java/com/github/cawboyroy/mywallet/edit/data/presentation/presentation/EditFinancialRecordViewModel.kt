package com.github.cawboyroy.mywallet.edit.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.add.presentation.Close
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.main.data.EditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class EditFinancialRecordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val runAsync: RunAsync,
    private val repository: EditRepository,
) : ViewModel() {

    private val closeState: MutableStateFlow<Close> = MutableStateFlow(Close.Empty)
    val close: StateFlow<Close>
        get() = closeState

    val state: StateFlow<EditState> = savedStateHandle.getStateFlow("state", EditState.Empty)


    fun loadRecord(id: Long) {
        runAsync.runAsync(
            scope = viewModelScope,
            background = { repository.record(id) }
        ) {
            savedStateHandle["state"] = EditState.Success(it)
        }
    }

    fun canSave(
        record: FinancialRecord,
        isExpenses: Boolean,
        money: String,
        title: String,
        category: String,
        time: Long,
        description: String,
        id: Long,
    ) = FinancialRecord(
        money.toDouble(),
        title,
        category,
        description,
        time,
        isExpenses,
        id
    ) != record

    fun delete(id: Long) {
        runAsync.runAsync(scope = viewModelScope, background = {
            repository.delete(id)
        }) {
            closeState.value = Close.Back
        }
    }

    fun edit(
        id: Long,
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
                    id
                )
            )
        }) {
            closeState.value = Close.Back
        }
    }
}

interface EditState : Serializable {

    @Composable
    fun Show(
        id: Long,
        viewModel: EditFinancialRecordViewModel,
        navController: NavController,
    ) = Unit

    data object Empty : EditState {
        private fun readResolve(): Any = Empty

        @Composable
        override fun Show(
            id: Long,
            viewModel: EditFinancialRecordViewModel,
            navController: NavController,
        ) {
            viewModel.loadRecord(id)
        }
    }

    data class Success(private val record: FinancialRecord) : EditState {

        @Composable
        override fun Show(
            id: Long,
            viewModel: EditFinancialRecordViewModel,
            navController: NavController,
        ) {
            EditFinancialRecordInner(viewModel, record, navController)
        }
    }
}