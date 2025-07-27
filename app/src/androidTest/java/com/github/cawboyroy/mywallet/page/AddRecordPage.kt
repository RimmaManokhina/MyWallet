package com.github.cawboyroy.mywallet.page

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.requestFocus

class AddRecordPage(private val composeTestRule: ComposeTestRule) {

    private val saveButton = composeTestRule.onNodeWithText("Save")
    private val currency = composeTestRule.onNodeWithTag("CurrencyBeforeMoneyInput")
    private val moneyInput = composeTestRule.onNodeWithTag("MoneyInputField")
    private val titleInput = composeTestRule.onNodeWithTag("RecordTitleInputField")
    private val categoryInput = composeTestRule.onNodeWithTag("RecordCategoryInputField")

    fun checkCurrency(value: String) = currency.assertTextEquals(value)
    fun checkSaveButtonEnabled() = saveButton.assertIsEnabled()
    fun checkSaveButtonDisabled() = saveButton.assertIsNotEnabled()
    fun clickOnSaveButton() = saveButton.performClick()
    fun inputMoney(value: String) = moneyInput.performTextInput(value)
    fun checkMoneyInput(value: String) = moneyInput.assertTextEquals("1,000")
    fun inputTitle(title: String) = titleInput.performTextInput(title)
    fun inputCategory(category: String) = categoryInput.performTextInput(category)
    fun checkCategoryInput(value: String) = categoryInput.assert(hasText(value))
    fun requestFocusOnCategoryInput() = categoryInput.requestFocus()
    fun clickOnSuggestion(index: Int) {
        composeTestRule.onNodeWithTag("Suggestion at $index", useUnmergedTree = true).performClick()
    }
}