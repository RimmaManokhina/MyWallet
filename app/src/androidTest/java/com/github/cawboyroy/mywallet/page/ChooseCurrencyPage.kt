package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput

class ChooseCurrencyPage(private val composeTestRule: ComposeTestRule) {

    private val saveButton = composeTestRule.onNodeWithText("Save")
    private val input = composeTestRule.onNodeWithTag("ChooseCurrencyScreenChooseCurrencyInput")

    fun input(text: String) = input.performTextInput(text)
    fun clickSaveButton() = saveButton.performClick()
    fun checkSaveButtonDisabled() = saveButton.assertIsNotEnabled()
    fun checkSaveButtonEnabled() = saveButton.assertIsEnabled()

    fun clickOn(currency: String) =
        composeTestRule.onNodeWithTag("CurrencyTag$currency").performClick()

    fun checkInput(text: String) = input.assert(hasText(text))

}