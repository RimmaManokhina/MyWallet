package com.github.cawboyroy.mywallet.page

import androidx.annotation.DrawableRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.cawboyroy.mywallet.main.presentation.DrawableResId

class HomePage(private val composeTestRule: ComposeTestRule) {

    private val monthTotal = composeTestRule.onNodeWithTag("ListContentMonthTotal")
    private val month = composeTestRule.onNodeWithTag("ListContentMonth")
    private val addButton = composeTestRule.onNodeWithTag("AddFAB")
    private val incomeTabToggle = composeTestRule.onNodeWithTag("ExpensesIncomeToggle Incomes")
    private val leftButton = composeTestRule.onNodeWithTag("LeftButton")

    fun checkMonth(text: String) = month.assertTextEquals(text)
    fun checkMonthTotal(text: String) = monthTotal.assertTextEquals(text)
    fun clickAdd() = addButton.performClick()
    fun checkDaySum(position: Int, sum: String, date: String) {
        composeTestRule.onNodeWithTag("DayUiSum at $position", useUnmergedTree = true)
            .assertTextEquals(sum)
        composeTestRule.onNodeWithTag("DayUiDate at $position", useUnmergedTree = true)
            .assertTextEquals(date)
    }

    fun checkRecord(
        position: Int,
        title: String,
        category: String,
        money: String,
        @DrawableRes drawableResId: Int,
    ) = with(composeTestRule) {
        onNodeWithTag("RecordTitle at $position", useUnmergedTree = true).assertTextEquals(title)
        onNodeWithTag("RecordCategory at $position", useUnmergedTree = true).assertTextEquals(
            category
        )
        onNodeWithTag("RecordMoney at $position", useUnmergedTree = true).assertTextEquals(money)
        onNodeWithTag(
            "RecordIcon at $position",
            useUnmergedTree = true
        ).assert(SemanticsMatcher.expectValue(DrawableResId, drawableResId))

    }

    fun chooseIncomes() = incomeTabToggle.performClick()

    fun clickLeft(times: Int) {
        repeat(times) {
            leftButton.performClick()
        }
    }
}