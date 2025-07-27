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
    private val addButton = composeTestRule.onNodeWithTag("HomeAddButton")

    fun checkMonthTotal(text: String) = monthTotal.assertTextEquals(text)
    fun clickAdd() = addButton.performClick()
    fun checkDaySum(position: Int, text: String) =
        composeTestRule.onNodeWithTag("DayUiSum at $position", useUnmergedTree = true)
            .assertTextEquals(text)

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
}