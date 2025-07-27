package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class MainPage(composeTestRule: ComposeTestRule) {

    private val homeTab = composeTestRule.onNodeWithTag("MainScreenBottomItem home")
    private val chartTab = composeTestRule.onNodeWithTag("MainScreenBottomItem chart")
    private val barsTab = composeTestRule.onNodeWithTag("MainScreenBottomItem bars")
    private val settingsTab = composeTestRule.onNodeWithTag("MainScreenBottomItem settings")


    fun clickHome() = homeTab.performClick()
    fun clickChart() = chartTab.performClick()
    fun clickBars() = barsTab.performClick()
    fun clickSettings() = settingsTab.performClick()
}