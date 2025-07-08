package com.github.cawboyroy.mywallet.settings.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.cawboyroy.mywallet.core.RunAsync
import com.github.cawboyroy.mywallet.settings.data.ImportExportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ImportExportViewModel @Inject constructor(
    private val runAsync: RunAsync,
    private val repository: ImportExportRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val state: StateFlow<ImportExportState> =
        savedStateHandle.getStateFlow(KEY, ImportExportState.Initial)

    fun import(uri: Uri) {
        savedStateHandle[KEY] = ImportExportState.ImportInProgress(uri)
    }

    fun importInternal(uri: Uri) {
        runAsync.runAsync(viewModelScope, {
            if (repository.import(uri))
                ImportExportState.Imported
            else
                ImportExportState.Failed
        }) {
            savedStateHandle[KEY] = it
        }
    }

    fun export() {
        savedStateHandle[KEY] = ImportExportState.ExportInProgress
    }

    fun exportInternal() {
        runAsync.runAsync(viewModelScope, {
            repository.export()
        }) {
            savedStateHandle[KEY] = ImportExportState.Share(it)
        }
    }

    companion object {
        private const val KEY = "ImportExportState"
    }
}