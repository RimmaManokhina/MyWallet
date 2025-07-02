package com.github.cawboyroy.mywallet.add.presentation

import androidx.lifecycle.ViewModel
import com.github.cawboyroy.mywallet.add.data.ListRepository
import com.github.cawboyroy.mywallet.core.RunAsync

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    runAsync: RunAsync,
    repository: ListRepository,
) : ViewModel() {

    private val mutableState = MutableStateFlow<List<String>>(emptyList())
    val state: StateFlow<List<String>>
        get() = mutableState

    init {
        runAsync.runFlow(
            scope = viewModelScope,
            flow = repository.list()
        ) {
            mutableState.value = it
        }
    }
}