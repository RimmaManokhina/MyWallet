package com.github.cawboyroy.mywallet.main.data

import android.content.Context
import androidx.room.Room
import com.github.cawboyroy.mywallet.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface CacheModule {

    fun dao(): FinancialRecordsDao

    @Singleton
    class Base @Inject constructor(@ApplicationContext applicationContext: Context) : CacheModule {

        private val database by lazy {
            Room.databaseBuilder(
                applicationContext,
                FinancialRecordsDatabase::class.java,
                applicationContext.getString(R.string.app_name)
            ).build()
        }

        override fun dao() = database.dao()
    }
}