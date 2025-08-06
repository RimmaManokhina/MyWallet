package com.github.cawboyroy.mywallet

import android.content.Context
import android.icu.util.Calendar
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.cawboyroy.mywallet.di.FakeTime
import com.github.cawboyroy.mywallet.di.TimeModule
import com.github.cawboyroy.mywallet.main.data.FinancialRecordsDatabase
import com.github.cawboyroy.mywallet.page.AddRecordPage
import com.github.cawboyroy.mywallet.page.BarsPage
import com.github.cawboyroy.mywallet.page.ChartPage
import com.github.cawboyroy.mywallet.page.ChooseCurrencyPage
import com.github.cawboyroy.mywallet.page.HomePage
import com.github.cawboyroy.mywallet.page.MainPage
import com.github.cawboyroy.mywallet.page.SettingsPage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(TimeModule::class)
@HiltAndroidTest
class ScenarioTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeTime: FakeTime

    @Before
    fun setUp() {
        hiltRule.inject()
        val context = ApplicationProvider.getApplicationContext<Context>()
        Room.databaseBuilder(
            context,
            FinancialRecordsDatabase::class.java,
            context.getString(R.string.app_name)
        ).build().clearAllTables()
    }

    @Test
    fun addExpense() {
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
        homePage.checkMonth(text = "June")

        homePage.clickAdd()
        val addRecordPage = AddRecordPage(composeTestRule)
        addRecordPage.checkCurrency(value = "RUB")
        addRecordPage.checkTime("Jun 27, 2025 09:30")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputMoney(value = "1000")
        addRecordPage.checkMoneyInput(value = "1,000")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputTitle(title = "bread")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.checkCategoryInput(value = "")
        addRecordPage.requestFocusOnCategoryInput()
        addRecordPage.clickOnSuggestion(id = 1)
        addRecordPage.checkCategoryInput("Groceries")
        addRecordPage.checkSaveButtonEnabled()
        addRecordPage.clickOnSaveButton()
        homePage.checkMonthTotal(text = "RUB 1,000")
        homePage.checkDaySum(position = 0, sum = "RUB 1,000", date = "June 27")
        homePage.checkRecord(
            position = 1,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            drawableResId = R.drawable.ic_category_groceries
        )
        mainPage.clickChart()
        val chartPage = ChartPage(composeTestRule)
        chartPage.checkMonthTotal(sum = "RUB 1,000")
        chartPage.checkMonth(text = "June")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 100.00%\nRUB 1,000"
        )
        chartPage.clickOnCollapsableHeader(0)
        chartPage.checkRecordDetail(
            position = 1,
            drawableResId = R.drawable.ic_category_groceries,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            time = "Jun 27, 2025 09:30"
        )

        mainPage.clickBars()
        val barsPage = BarsPage(composeTestRule)
        barsPage.checkYearTotal(year = "2025", money = "RUB 1,000")
        barsPage.checkBarText(position = 0, "June\nRUB 1,000")
    }

    @Test
    fun addSecondExpense() {
        addExpense()
        val mainPage = MainPage(composeTestRule)
        mainPage.clickHome()
        val homePage = HomePage(composeTestRule)
        fakeTime.setTime(
            2025, Calendar.JUNE, 27,
            10, 12, 20
        )
        homePage.clickAdd()
        val addRecordPage = AddRecordPage(composeTestRule)
        addRecordPage.checkCurrency(value = "RUB")
        addRecordPage.checkTime("Jun 27, 2025 10:12")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputMoney(value = "2000")
        addRecordPage.checkMoneyInput(value = "2,000")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputTitle(title = "apples")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.checkCategoryInput(value = "")
        addRecordPage.requestFocusOnCategoryInput()
        addRecordPage.clickOnSuggestion(id = 1)
        addRecordPage.checkCategoryInput("Groceries")
        addRecordPage.checkSaveButtonEnabled()
        addRecordPage.clickOnSaveButton()

        homePage.checkMonthTotal(text = "RUB 3,000")
        homePage.checkDaySum(position = 0, sum = "RUB 3,000", date = "June 27")
        homePage.checkRecord(
            position = 1,
            title = "apples",
            category = "Groceries",
            money = "RUB 2,000",
            drawableResId = R.drawable.ic_category_groceries
        )
        homePage.checkRecord(
            position = 2,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            drawableResId = R.drawable.ic_category_groceries
        )
        mainPage.clickChart()
        val chartPage = ChartPage(composeTestRule)
        chartPage.checkMonthTotal(sum = "RUB 3,000")
        chartPage.checkMonth(text = "June")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 100.00%\nRUB 3,000"
        )
        chartPage.clickOnCollapsableHeader(0)
        chartPage.checkRecordDetail(
            position = 1,
            drawableResId = R.drawable.ic_category_groceries,
            title = "apples",
            category = "Groceries",
            money = "RUB 2,000",
            time = "Jun 27, 2025 10:12"
        )
        chartPage.checkRecordDetail(
            position = 2,
            drawableResId = R.drawable.ic_category_groceries,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            time = "Jun 27, 2025 09:30"
        )
        mainPage.clickBars()
        val barsPage = BarsPage(composeTestRule)
        barsPage.checkYearTotal(year = "2025", money = "RUB 3,000")
        barsPage.checkBarText(position = 0, "June\nRUB 3,000")
    }

    @Test
    fun addSecondExpenseAnotherCategory() {
        addExpense()
        val mainPage = MainPage(composeTestRule)
        mainPage.clickHome()
        val homePage = HomePage(composeTestRule)
        fakeTime.setTime(
            2025, Calendar.JUNE, 27,
            10, 12, 20
        )
        homePage.clickAdd()
        val addRecordPage = AddRecordPage(composeTestRule)
        addRecordPage.checkCurrency(value = "RUB")
        addRecordPage.checkTime("Jun 27, 2025 10:12")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputMoney(value = "3000")
        addRecordPage.checkMoneyInput(value = "3,000")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputTitle(title = "kfc")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.checkCategoryInput(value = "")
        addRecordPage.requestFocusOnCategoryInput()
        addRecordPage.clickOnSuggestion(id = 2)
        addRecordPage.checkCategoryInput("Dining out")
        addRecordPage.checkSaveButtonEnabled()
        addRecordPage.clickOnSaveButton()

        homePage.checkMonthTotal(text = "RUB 4,000")
        homePage.checkDaySum(position = 0, sum = "RUB 4,000", date = "June 27")
        homePage.checkRecord(
            position = 1,
            title = "kfc",
            category = "Dining out",
            money = "RUB 3,000",
            drawableResId = R.drawable.ic_category_dining_out
        )
        homePage.checkRecord(
            position = 2,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            drawableResId = R.drawable.ic_category_groceries
        )
        mainPage.clickChart()
        val chartPage = ChartPage(composeTestRule)
        chartPage.checkMonthTotal(sum = "RUB 4,000")
        chartPage.checkMonth(text = "June")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_dining_out,
            details = "Dining out 75.00%\nRUB 3,000"
        )
        chartPage.checkCategoryHeader(
            position = 1,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 25.00%\nRUB 1,000"
        )
        chartPage.clickOnCollapsableHeader(0)
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_dining_out,
            details = "Dining out 75.00%\nRUB 3,000"
        )
        chartPage.checkRecordDetail(
            position = 1,
            drawableResId = R.drawable.ic_category_dining_out,
            title = "kfc",
            category = "Dining out",
            money = "RUB 3,000",
            time = "Jun 27, 2025 10:12"
        )
        chartPage.checkCategoryHeader(
            position = 2,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 25.00%\nRUB 1,000"
        )

        chartPage.clickOnIconInDonut(label = "Groceries")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 25.00%\nRUB 1,000"
        )
        chartPage.checkCategoryHeader(
            position = 1,
            drawableResId = R.drawable.ic_category_dining_out,
            details = "Dining out 75.00%\nRUB 3,000"
        )

        chartPage.clickOnCollapsableHeader(0)
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 25.00%\nRUB 1,000"
        )
        chartPage.checkRecordDetail(
            position = 1,
            drawableResId = R.drawable.ic_category_groceries,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            time = "Jun 27, 2025 09:30"
        )
        chartPage.checkCategoryHeader(
            position = 2,
            drawableResId = R.drawable.ic_category_dining_out,
            details = "Dining out 75.00%\nRUB 3,000"
        )

        mainPage.clickBars()
        val barsPage = BarsPage(composeTestRule)
        barsPage.checkYearTotal(year = "2025", money = "RUB 4,000")
        barsPage.checkBarText(position = 0, "June\nRUB 4,000")
    }

    @Test
    fun addIncomeAfterExpense() {
        addExpense()
        val mainPage = MainPage(composeTestRule)
        mainPage.clickHome()
        val homePage = HomePage(composeTestRule)
        fakeTime.setTime(
            2025, Calendar.JUNE, 27,
            10, 12, 20
        )
        homePage.clickAdd()
        val addRecordPage = AddRecordPage(composeTestRule)
        addRecordPage.checkCurrency(value = "RUB")
        addRecordPage.checkTime("Jun 27, 2025 10:12")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.chooseIncomes()
        addRecordPage.inputMoney(value = "3000")
        addRecordPage.checkMoneyInput(value = "3,000")
        addRecordPage.checkSaveButtonDisabled()

        addRecordPage.inputTitle(title = "restaurant tips")
        addRecordPage.checkSaveButtonDisabled()
        addRecordPage.checkCategoryInput(value = "")
        addRecordPage.requestFocusOnCategoryInput()
        addRecordPage.clickOnSuggestion(id = 29)
        addRecordPage.checkCategoryInput("Tips")
        addRecordPage.checkSaveButtonEnabled()
        addRecordPage.clickOnSaveButton()

        homePage.checkMonthTotal(text = "RUB 1,000")
        homePage.checkDaySum(position = 0, sum = "RUB 1,000", date = "June 27")
        homePage.checkRecord(
            position = 1,
            title = "bread",
            category = "Groceries",
            money = "RUB 1,000",
            drawableResId = R.drawable.ic_category_groceries
        )

        homePage.chooseIncomes()
        homePage.checkMonthTotal(text = "RUB 3,000")
        homePage.checkDaySum(position = 0, sum = "RUB 3,000", date = "June 27")
        homePage.checkRecord(
            position = 1,
            title = "restaurant tips",
            category = "Tips",
            money = "RUB 3,000",
            drawableResId = R.drawable.ic_payment_arrow_down
        )

        mainPage.clickChart()
        val chartPage = ChartPage(composeTestRule)
        chartPage.checkMonthTotal(sum = "RUB 1,000")
        chartPage.checkMonth(text = "June")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_category_groceries,
            details = "Groceries 100.00%\nRUB 1,000"
        )
        chartPage.chooseIncomes()
        chartPage.checkMonthTotal(sum = "RUB 3,000")
        chartPage.checkMonth(text = "June")
        chartPage.checkCategoryHeader(
            position = 0,
            drawableResId = R.drawable.ic_payment_arrow_down,
            details = "Tips 100.00%\nRUB 3,000"
        )

        mainPage.clickBars()
        val barsPage = BarsPage(composeTestRule)
        barsPage.checkYearTotal(year = "2025", money = "RUB 1,000")
        barsPage.checkBarText(position = 0, "June\nRUB 1,000")

        barsPage.chooseIncomes()
        barsPage.checkYearTotal(year = "2025", money = "RUB 3,000")
        barsPage.checkBarText(position = 0, "June\nRUB 3,000")
    }

    @Test
    fun addTwelveExpensesAndTwelveIncomesForAYear() {
        val mainPage = MainPage(composeTestRule)
        mainPage.clickSettings()
        val settingsPage = SettingsPage(composeTestRule)
        settingsPage.clickChooseCurrency()
        val chooseCurrencyPage = ChooseCurrencyPage(composeTestRule)
        chooseCurrencyPage.input(text = "$")
        chooseCurrencyPage.clickSaveButton()

        val homePage = HomePage(composeTestRule)
        val addRecordPage = AddRecordPage(composeTestRule)
        val barsPage = BarsPage(composeTestRule)

        val sums = listOf(
            "January\n$ 1,000",
            "February\n$ 2,000",
            "March\n$ 3,000",
            "April\n$ 4,000",
            "May\n$ 5,000",
            "June\n$ 6,000",
            "July\n$ 7,000",
            "August\n$ 8,000",
            "September\n$ 9,000",
            "October\n$ 10,000",
            "November\n$ 11,000",
            "December\n$ 12,000"
        )
        var sum = 0
        repeat(12) {
            mainPage.clickHome()
            val date = Date(
                2024,
                it,
                5,
                13,
                15,
                44
            )
            date.changeTime(fakeTime)
            homePage.clickAdd()
            val money = 1000 * (it + 1)
            sum += (it + 1)

            addRecordPage.inputMoney(value = money.toString())
            addRecordPage.inputTitle(title = "bread")
            addRecordPage.requestFocusOnCategoryInput()
            addRecordPage.clickOnSuggestion(id = 1)

            addRecordPage.clickOnSaveButton()
            mainPage.clickBars()
            barsPage.checkBarText(position = it, text = sums[it])
            barsPage.checkYearTotal(year = "2024", money = "$ $sum,000")
        }
    }
}

private data class Date(
    private val year: Int,
    private val month: Int,
    private val day: Int,
    private val hour: Int,
    private val minutes: Int,
    private val seconds: Int,
) {

    fun changeTime(fakeTime: FakeTime) {
        fakeTime.setTime(year, month, day, hour, minutes, seconds)
    }
}