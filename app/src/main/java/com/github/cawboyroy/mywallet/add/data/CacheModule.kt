package com.github.cawboyroy.mywallet.add.data

import android.content.Context
import androidx.room.Room
import com.github.cawboyroy.mywallet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface CacheModule {

    fun dao(): ExpensesDao

    class Base @Inject constructor(@ApplicationContext applicationContext: Context) : CacheModule {

        private val database by lazy {
            Room.databaseBuilder(
                applicationContext,
                ExpensesDatabase::class.java,
                applicationContext.getString(R.string.app_name)
            ).build()
        }

        override fun dao() = database.dao()
    }
}