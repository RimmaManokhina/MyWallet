package com.github.cawboyroy.mywallet.currency.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.cawboyroy.mywallet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface ChosenCurrencyRepository {

    fun value(): Flow<String>

    suspend fun save(value: String)

    @Singleton
    class Base @Inject constructor(
        @ApplicationContext private val context: Context
    ) : ChosenCurrencyRepository {

        private val Context.dataStore by preferencesDataStore(name = context.getString(R.string.app_name))
        private val preferencesKey = stringPreferencesKey("currency")

        override fun value(): Flow<String> {
            return context.dataStore.data.map { preferences ->
                preferences[preferencesKey] ?: ""
            }
        }

        override suspend fun save(value: String) {
            context.dataStore.edit { preferences ->
                preferences[preferencesKey] = value
            }
        }
    }
}