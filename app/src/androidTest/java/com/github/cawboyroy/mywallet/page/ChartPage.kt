package com.github.cawboyroy.mywallet.page

import androidx.annotation.DrawableRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.cawboyroy.mywallet.main.presentation.DrawableResId

class ChartPage(private val composeTestRule: ComposeTestRule) {

    private val monthTotal = composeTestRule.onNodeWithTag("ChartScreenMonthTotal")

    fun checkMonthTotal(sum: String) = monthTotal.assertTextEquals(sum)

    fun checkCategoryHeader(
        position: Int,
        @DrawableRes drawableResId: Int,
        details: String
    ) {
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
        money: String
    ) = with(composeTestRule) {
        onNodeWithTag("ChartRecordIcon at $position", useUnmergedTree = true)
            .assert(SemanticsMatcher.expectValue(DrawableResId, drawableResId))
        onNodeWithTag("ChartRecordTitle at $position", useUnmergedTree = true)
            .assertTextEquals(title)
        onNodeWithTag("ChartRecordCategory at $position", useUnmergedTree = true)
            .assertTextEquals(category)
        onNodeWithTag("ChartRecordMoney at $position", useUnmergedTree = true)
            .assertTextEquals(money)
    }
}