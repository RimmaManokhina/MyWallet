package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

class SettingsPage(composeTestRule: ComposeTestRule) {

    private val chooseCurrencyButton =
        composeTestRule.onNodeWithTag("SettingsScreenChooseCurrencyButton")

    fun clickChooseCurrency() = chooseCurrencyButton.performClick()
    fun checkVisible() = chooseCurrencyButton.assertIsDisplayed()
}