package com.github.cawboyroy.mywallet.bars

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import com.github.cawboyroy.mywallet.main.data.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Month
import java.time.format.TextStyle.FULL
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BarsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val chosenCurrencyRepository: ChosenCurrencyRepository,
    private val repository: ListRepository,
    runAsync: RunAsync,
) : ViewModel() {

    fun chosenCurrency() = chosenCurrencyRepository.value()

    private val recordsMutableStateFlow =
        MutableStateFlow<PersistentList<MonthSummaryUi>>(persistentListOf())
    val recordsFlow: StateFlow<PersistentList<MonthSummaryUi>>
        get() = recordsMutableStateFlow

    val screenStateFlow = savedStateHandle.getStateFlow(
        SCREEN_STATE, BarsScreenState(
            isExpenses = true,
            time = YearsUi(System.currentTimeMillis()),
        )
    )

    init {
        runAsync.runFlow<PersistentList<MonthSummaryUi>>(
            scope = viewModelScope,
            flow = makeFlow()
        ) {
            recordsMutableStateFlow.value = it
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun makeFlow(): Flow<PersistentList<MonthSummaryUi>> {
        return screenStateFlow.flatMapLatest { screenState ->
            val (min, max) = screenState.time.yearBoundaries()
            repository.list(screenState.isExpenses, min, max).map { list ->
                if (list.isEmpty())
                    return@map emptyList<MonthSummaryUi>().toPersistentList()
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                val grouped = list.groupBy {
                    calendar.timeInMillis = it.time
                    calendar.get(Calendar.MONTH)
                }
                val final = mutableListOf<Pair<Int, BigDecimal>>()
                grouped.forEach { month, money ->
                    final.add(Pair(month, money.sumOf { BigDecimal(it.money) }))
                }
                val max2 = final.maxBy { it.second }
                final.map {
                    val multiply: BigDecimal = it.second
                    MonthSummaryUi(
                        multiply.divide(max2.second, 2, RoundingMode.HALF_UP).toFloat(),
                        it.first,
                        it.second.toString()
                    )
                }.toPersistentList()
            }
        }
    }

    fun switch(isExpenses: Boolean) {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.switch(isExpenses)
    }

    fun showNextYear() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.showNextYear()
    }

    fun showPreviousYear() {
        savedStateHandle[SCREEN_STATE] = screenStateFlow.value.showPreviousYear()
    }

    companion object {
        private const val SCREEN_STATE = "BARS screen state"
    }
}

data class MonthSummaryUi(
    val percentage: Float,
    private val month: Int,
    val sum: String,
) {

    fun ui(currency: String): String {
        val monthUi = Month.of(month + 1).getDisplayName(FULL, Locale.getDefault())
        return monthUi + "\n" + HandleMoney.formatWhole(currency, sum)
    }

}