package com.github.cawboyroy.mywallet.page

import androidx.annotation.DrawableRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.github.cawboyroy.mywallet.main.presentation.DrawableResId

class ChartPage(private val composeTestRule: ComposeTestRule) {

    private val monthTotal = composeTestRule.onNodeWithTag("ChartScreenMonthTotal")
    private val month = composeTestRule.onNodeWithTag("ChartScreenMonth")
    private val incomeTabInToggle = composeTestRule.onNodeWithTag("ExpensesIncomeToggle Incomes")

    fun checkMonthTotal(sum: String) = monthTotal.assertTextEquals(sum)
    fun checkMonth(text: String) = month.assertTextEquals(text)

    fun checkCategoryHeader(
        position: Int,
        @DrawableRes drawableResId: Int,
        details: String
    ) {
        composeTestRule.onNodeWithTag("ChartScreenContentLazyColumn")
            .performScrollToNode(hasTestTag("ChartScreenHeader at $position"))
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag("CategoryHeaderIcon at $position", useUnmergedTree = true)
            .assert(SemanticsMatcher.expectValue(DrawableResId, drawableResId))
        composeTestRule.onNodeWithTag("CategoryHeaderDetails at $position", useUnmergedTree = true)
            .assertTextEquals(details)
    }

    fun clickOnCollapsableHeader(position: Int) {
        composeTestRule.onNodeWithTag("CategoryHeaderCollapsableIcon at $position").performClick()
    }

    fun checkRecordDetail(
        position: Int,
        @DrawableRes drawableResId: Int,
        title: String,
        category: String,
        money: String,
        time: String
    ) = with(composeTestRule) {
        onNodeWithTag("ChartScreenContentLazyColumn")
            .performScrollToNode(hasTestTag("ChartScreenItem at $position"))
            .assertIsDisplayed()
        onNodeWithTag("ChartRecordIcon at $position", useUnmergedTree = true)
            .assert(SemanticsMatcher.expectValue(DrawableResId, drawableResId))
        onNodeWithTag("ChartRecordTitle at $position", useUnmergedTree = true)
            .assertTextEquals(title)
        onNodeWithTag("ChartRecordCategory at $position", useUnmergedTree = true)
            .assertTextEquals(category)
        onNodeWithTag("ChartRecordMoney at $position", useUnmergedTree = true)
            .assertTextEquals(money)
        onNodeWithTag("ChartRecordTime at $position", useUnmergedTree = true)
            .assertTextEquals(time)
    }

    fun clickOnIconInDonut(label: String) {
        composeTestRule.onNodeWithTag("ChartScreenContentLazyColumn")
            .performScrollToNode(hasTestTag("PieChartInLazyColumn"))
        composeTestRule.onNodeWithTag("PieChartSegmentIcon $label")
            .performClick()
    }

    fun chooseIncomes() = incomeTabInToggle.performClick()
}