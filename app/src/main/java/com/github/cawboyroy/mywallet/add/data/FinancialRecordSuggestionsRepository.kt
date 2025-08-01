package com.github.cawboyroy.mywallet.add.data

import com.github.cawboyroy.mywallet.main.data.FinancialRecordsDao
import javax.inject.Inject

interface FinancialRecordSuggestionsRepository {

    suspend fun find(userInput: String, isExpenses: Boolean): List<Pair<String, String>>

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao
    ) : FinancialRecordSuggestionsRepository {

        override suspend fun find(
            userInput: String,
            isExpenses: Boolean
        ): List<Pair<String, String>> {
            return dao.financialRecordsStartingWith(userInput, isExpenses).map {
                Pair(
                    it.title,
                    it.category,
                )
            }.distinct()
        }
    }
}