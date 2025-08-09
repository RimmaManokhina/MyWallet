package com.github.cawboyroy.mywallet.main.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.di.ProvideTime
import com.github.cawboyroy.mywallet.main.domain.ListInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    runAsync: RunAsync,
    private val interactor: ListInteractor,
    private val mapper: HomeUiStateMapper,
    provideTime: ProvideTime,
) : ViewModel(), RecordActions, AllDayActions {

    private val screenStateMutableStateFlow =
        MutableStateFlow(
            HomeScreenState(
                true,
                "",
                "",
                AllCollapsedUi.Expanded,
                persistentListOf()
            )
        )
    val screenState: StateFlow<HomeScreenState>
        get() = screenStateMutableStateFlow

    private val homeScreenParams = savedStateHandle.getStateFlow(
        key = SCREEN_STATE,
        initialValue = HomeScreenParams(
            allCollapsed = false,
            time = provideTime.now(),
            isExpenses = true,
            collapsedIds = CollapsedIds()
        )
    )

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = homeScreenParams.combine(interactor.currency()) { screenParams, currency ->
                Pair(screenParams, currency)
            }.flatMapLatest { (screenParams, currency) ->
                interactor.list(screenParams).map { list ->
                    mapper.map(list, screenParams, currency)
                }
            }
        ) {
            screenStateMutableStateFlow.value = it
        }
    }

    override fun expandAll() {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.expandAll()
    }

    override fun collapseAll() {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.collapseAll()
    }

    fun switch(isExpenses: Boolean) {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.switch(isExpenses)
    }

    fun showPreviousMonth() {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.showPreviousMonth()
    }

    fun showNextMonth() {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.showNextMonth()
    }

    override fun collapse(id: Int) {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.collapse(id)
    }

    override fun expand(id: Int) {
        savedStateHandle[SCREEN_STATE] = homeScreenParams.value.expand(id)
    }

    companion object Companion {
        private const val SCREEN_STATE = "SCREEN_STATE"
    }
}