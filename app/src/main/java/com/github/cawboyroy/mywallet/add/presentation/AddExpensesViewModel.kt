package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.add.data.AddRepository
import com.github.cawboyroy.mywallet.core.RunAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class AddFinancialRecordViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val addRepository: AddRepository,
) : ViewModel() {

    private val closeState: MutableStateFlow<Close> = MutableStateFlow(Close.Empty)
    val close: StateFlow<Close>
        get() = closeState

    fun add(
        money: String,
        title: String,
        category: String,
        description: String,
        time: Long,
        isExpenses: Boolean
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
            closeState.value = Close.Back
        }
    }

    fun canSave(
        money: String,
        title: String,
        category: String
    ): Boolean {
        return money.isNotEmpty() && title.isNotEmpty() && category.isNotEmpty()
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
) : Serializable

interface Close {

    @Composable
    fun Show(navController: NavController) = Unit

    data object Empty : Close

    data object Back : Close {

        @Composable
        override fun Show(navController: NavController) {
            navController.popBackStack()
        }
    }
}