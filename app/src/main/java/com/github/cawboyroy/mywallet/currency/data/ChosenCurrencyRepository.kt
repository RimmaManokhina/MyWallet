package com.github.cawboyroy.mywallet.currency.data

import android.content.Context
import androidx.core.content.edit
import com.github.cawboyroy.mywallet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ChosenCurrencyRepository {

    fun value(): String

    fun save(value: String)

    class Base @Inject constructor(
        @ApplicationContext context: Context,
    ) : ChosenCurrencyRepository {

        private val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_name), Context.MODE_PRIVATE
        )

        override fun value(): String {
            return sharedPreferences.getString("chosen currency", "") ?: ""
        }

        override fun save(value: String) {
            sharedPreferences.edit { putString("chosen currency", value) }
        }
    }
}