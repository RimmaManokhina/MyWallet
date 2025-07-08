package com.github.cawboyroy.mywallet

import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HandleMoneyTest {

    @Test
    fun value() = with(HandleMoney) {
        assertEquals("1234", value("1234"))
        assertEquals("1234", value("000001234"))
        assertEquals("1234", value("asdf1234"))
        assertEquals("1234.98", value("asdf1234,.98.56"))
        assertEquals("0.0", value("0.00"))
        assertEquals("0.", value("asd.asdf,asd."))
    }

    @Test
    fun ui() = with(HandleMoney) {
        assertEquals("", ui(""))
        assertEquals("", ui("0"))
        assertEquals(".", ui("."))
        assertEquals("1,234.56", ui("1234.56"))
        assertEquals("123,456", ui("123456"))
    }

    @Test
    fun formatWhole() = with(HandleMoney) {
        assertEquals("$ 0", formatWhole("$", ""))
        assertEquals("$ 123", formatWhole("$", "123"))
        assertEquals("$ 1,235.4", formatWhole("$", "1235.4"))
        assertEquals("$ 1,235", formatWhole("$", "1235."))
        assertEquals("$ 1,235", formatWhole("$", "1235.0"))
        assertEquals("1,235", formatWhole("", "1235.0"))
    }

    @Test
    fun finalize() = with(HandleMoney) {
        assertEquals("123", finalize("123."))
        assertEquals("123", finalize("123.0"))
        assertEquals("123.01", finalize("123.01"))
        assertEquals("123.1", finalize("123.1"))
    }
}