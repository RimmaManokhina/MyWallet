package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag

class BarsPage(private val composeTestRule: ComposeTestRule) {

    private val monthTotal = composeTestRule.onNodeWithTag("BarsMonthTotal")

    fun checkMonthTotal(money: String) {
        monthTotal.assertTextEquals(money)
    }

    fun checkBarTextContains(position: Int, text: String) {
        composeTestRule.onNodeWithTag("BarText at $position", useUnmergedTree = true)
            .assertTextContains(text, substring = true)
    }

}