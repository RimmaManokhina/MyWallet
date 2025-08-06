package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class BarsPage(private val composeTestRule: ComposeTestRule) {

    private val year = composeTestRule.onNodeWithTag("BarsYear")
    private val yearTotal = composeTestRule.onNodeWithTag("BarsYearTotal")
    private val incomeTabInToggle = composeTestRule.onNodeWithTag("ExpensesIncomeToggle Incomes")

    fun checkYearTotal(year: String, money: String) {
        this.year.assertTextEquals(year)
        yearTotal.assertTextEquals(money)
    }

    fun checkBarText(position: Int, text: String) {
        composeTestRule.onNodeWithTag("BarText at $position", useUnmergedTree = true)
            .assertTextEquals(text)
    }
    fun chooseIncomes() = incomeTabInToggle.performClick()
}