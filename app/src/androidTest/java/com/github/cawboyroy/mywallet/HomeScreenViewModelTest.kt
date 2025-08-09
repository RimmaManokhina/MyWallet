package com.github.cawboyroy.mywallet

import androidx.lifecycle.SavedStateHandle
import com.github.cawboyroy.mywallet.add.presentation.FinancialRecord
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.currency.data.ChosenCurrencyRepository
import com.github.cawboyroy.mywallet.di.ProvideTime
import com.github.cawboyroy.mywallet.main.domain.ListInteractor
import com.github.cawboyroy.mywallet.main.presentation.AllCollapsedUi
import com.github.cawboyroy.mywallet.main.presentation.CollapsedIds
import com.github.cawboyroy.mywallet.main.presentation.FinancialRecordUi
import com.github.cawboyroy.mywallet.main.presentation.HomeScreenParams
import com.github.cawboyroy.mywallet.main.presentation.HomeScreenState
import com.github.cawboyroy.mywallet.main.presentation.HomeScreenViewModel
import com.github.cawboyroy.mywallet.main.presentation.HomeUiStateMapper
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.math.BigDecimal

class HomeScreenViewModelTest {

    @Test
    fun test() = runBlocking {
        val listInteractor = FakeListInteractor()
        val savedStateHandle = SavedStateHandle()
        val viewModel = HomeScreenViewModel(
            savedStateHandle,
            FakeRunAsync(),
            listInteractor,
            FakeHomeUiStateMapper(),
            FakeProvideTime()
        )

        assertEquals(
            HomeScreenParams(false, 1234567890, true, CollapsedIds()),
            savedStateHandle["SCREEN_STATE"]
        )

        assertEquals(
            HomeScreenState(
                true, "", "1000", AllCollapsedUi.Expanded,
                persistentListOf(
                    FinancialRecordUi.Base(
                        "$",
                        true,
                        "1000",
                        "bread",
                        "Groceries",
                        909
                    )
                )
            ),
            viewModel.screenState.value
        )

        viewModel.collapseAll()
        assertEquals(
            HomeScreenParams(true, 1234567890, true, CollapsedIds((1..31).toSet())),
            savedStateHandle["SCREEN_STATE"]
        )
        viewModel.expandAll()
        assertEquals(
            HomeScreenParams(false, 1234567890, true, CollapsedIds()),
            savedStateHandle["SCREEN_STATE"]
        )

        viewModel.switch(false)
        assertEquals(
            HomeScreenParams(false, 1234567890, false, CollapsedIds()),
            savedStateHandle["SCREEN_STATE"]
        )
        viewModel.collapse(10)
        assertEquals(
            HomeScreenParams(false, 1234567890, false, CollapsedIds(setOf(10))),
            savedStateHandle["SCREEN_STATE"]
        )
        viewModel.expand(10)
        assertEquals(
            HomeScreenParams(
                false, 1234567890, false, CollapsedIds(

                )
            ),
            savedStateHandle["SCREEN_STATE"]
        )

    }
}

private class FakeChosenCurrencyRepository : ChosenCurrencyRepository {
    private val flow = MutableStateFlow("$")

    override fun value(): Flow<String> {
        return flow
    }

    override suspend fun save(value: String) {
        flow.emit(value)
    }
}

private class FakeRunAsync : RunAsync {

    override fun <T : Any> runAsync(
        scope: CoroutineScope,
        background: suspend () -> T,
        ui: (T) -> Unit
    ) {
        runBlocking { ui.invoke(background.invoke()) }
    }

    override fun <T : Any> runFlow(
        scope: CoroutineScope,
        flow: Flow<T>,
        onEach: suspend (T) -> Unit
    ) {
        runBlocking {
            onEach(flow.first())
        }
    }
}

private class FakeListInteractor : ListInteractor {
    private val chosenCurrencyRepository = FakeChosenCurrencyRepository()

    private val expensesFlow = MutableStateFlow(
        listOf(
            FinancialRecord(
                "1000", "bread", "Groceries", "", 999, true, 909
            )
        )
    )

    private val incomesFlow = MutableStateFlow(
        listOf(
            FinancialRecord(
                "2000", "wage", "Salary", "", 777, false, 707
            )
        )
    )

    override fun list(homeScreenParams: HomeScreenParams): Flow<List<FinancialRecord>> {
        return if (homeScreenParams.isExpenses)
            expensesFlow
        else
            incomesFlow
    }

    override fun currency() = chosenCurrencyRepository.value()
}

private class FakeHomeUiStateMapper : HomeUiStateMapper {

    override suspend fun map(
        list: List<FinancialRecord>,
        homeScreenParams: HomeScreenParams,
        currency: String
    ): HomeScreenState {
        val total = list.sumOf { BigDecimal(it.money) }
        return HomeScreenState(
            homeScreenParams.isExpenses,
            "", total.toString(),
            if (homeScreenParams.allCollapsed) AllCollapsedUi.Collapsed else AllCollapsedUi.Expanded,
            list.map {
                FinancialRecordUi.Base(
                    currency,
                    it.isExpenses,
                    it.money,
                    it.title,
                    it.category,
                    it.id
                )
            }.toPersistentList()
        )
    }

}

private class FakeProvideTime : ProvideTime {
    override fun now(): Long {
        return 1234567890
    }
}