package com.github.cawboyroy.mywallet

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.cawboyroy.mywallet.page.AddRecordPage
import com.github.cawboyroy.mywallet.page.BarsPage
import com.github.cawboyroy.mywallet.page.ChartPage
import com.github.cawboyroy.mywallet.page.ChooseCurrencyPage
import com.github.cawboyroy.mywallet.page.HomePage
import com.github.cawboyroy.mywallet.page.MainPage
import com.github.cawboyroy.mywallet.page.SettingsPage
import org.junit.Rule
import org.junit.Test

class ScenarioTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun add() {
        val mainPage = MainPage(composeTestRule)
        mainPage.clickSettings()

        val settingsPage = SettingsPage(composeTestRule)
        settingsPage.clickChooseCurrency()

        val chooseCurrencyPage = ChooseCurrencyPage(composeTestRule)
        chooseCurrencyPage.checkSaveButtonDisabled()

        chooseCurrencyPage.input(text = "ru")
        chooseCurrencyPage.checkSaveButtonEnabled()

        chooseCurrencyPage.clickOn(currency = "RUB Russian Ruble")
        chooseCurrencyPage.checkInput(text = "RUB")

        chooseCurrencyPage.clickSaveButton()
        settingsPage.checkVisible()

        mainPage.clickHome()
        val homePage = HomePage(composeTestRule)
        homePage.checkMonthTotal(text = "RUB 0")

        homePage.clickAdd()
        val addRecordPage = AddRecordPage(composeTestRule)
        addRecordPage.checkCurrency(value = "RUB")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputMoney(value = "1000")
        addRecordPage.checkMoneyInput(value = "1,000")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputTitle(title = "bread")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.checkCategoryInput(value = "")
        addRecordPage.requestFocusOnCategoryInput()
        addRecordPage.clickOnSuggestion(index = 0)
        addRecordPage.checkCategoryInput("Groceries")
        addRecordPage.checkSaveButtonEnabled()
        addRecordPage.clickOnSaveButton()
        homePage.checkMonthTotal(text = "RUB 1,000")
        homePage.checkDaySum(position = 0, "RUB 1,000")
        homePage.checkRecord(
            position = 1,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            drawableResId = R.drawable.ic_category_groceries
        )
        mainPage.clickChart()
        val chartPage = ChartPage(composeTestRule)
        chartPage.checkMonthTotal("RUB 1,000")
        chartPage.checkCategoryHeader(
            0,
            R.drawable.ic_category_groceries,
            "Groceries 100.00%\nRUB 1,000"
        )
        chartPage.clickOnCollapsableHeader(0)
        chartPage.checkRecordDetail(
            1,
            R.drawable.ic_category_groceries,
            "bread",
            "Groceries",
            "RUB 1,000"
        )

        mainPage.clickBars()
        val barsPage = BarsPage(composeTestRule)
        barsPage.checkMonthTotal("RUB 1,000")
        barsPage.checkBarTextContains(0, "RUB 1,000")
    }
}