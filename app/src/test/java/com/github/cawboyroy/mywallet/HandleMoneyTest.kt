package com.github.cawboyroy.mywallet

import com.github.cawboyroy.mywallet.add.presentation.HandleMoney
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

class HandleMoneyTest {

    @Test
    fun test() = with(HandleMoney) {
        var result = split("0.0")
        assertEquals(BigInteger.ZERO, result.first)
        assertEquals("0", result.second)

        result = split(BigDecimal.ZERO.toString())
        assertEquals(BigInteger.ZERO, result.first)
        assertEquals("", result.second)

        result = split("0.08")
        assertEquals(BigInteger("0"), result.first)
        assertEquals("08", result.second)

        result = split("123.45")
        assertEquals(BigInteger("123"), result.first)
        assertEquals("45", result.second)

        result = split("123456.05")
        assertEquals(BigInteger("123456"), result.first)
        assertEquals("05", result.second)
    }

    @Test
    fun format() = with(HandleMoney) {
        assertEquals("", formatMainPart(BigInteger.ZERO))
        assertEquals("1", formatMainPart(BigInteger("1")))
        assertEquals("12", formatMainPart(BigInteger("12")))
        assertEquals("123", formatMainPart(BigInteger("123")))
        assertEquals("1,234", formatMainPart(BigInteger("1234")))
        assertEquals("12,345", formatMainPart(BigInteger("12345")))
        assertEquals("123,456", formatMainPart(BigInteger("123456")))
        assertEquals("1,234,567", formatMainPart(BigInteger("1234567")))
        assertEquals("12,345,678", formatMainPart(BigInteger("12345678")))
        assertEquals("123,456,789", formatMainPart(BigInteger("123456789")))
        assertEquals("1,234,567,890", formatMainPart(BigInteger("1234567890")))
    }

    @Test
    fun cents() = with(HandleMoney) {
        assertEquals("", handleCents(""))
        assertEquals("0", handleCents("00"))
        assertEquals("01", handleCents("01"))
        assertEquals("45", handleCents("45"))
        assertEquals("45", handleCents("453"))
    }

    @Test
    fun handleMainPart() = with(HandleMoney) {
        assertEquals("0", handleMainPart("adsf"))
        assertEquals("10", handleMainPart("1slsdf0"))
    }

    @Test
    fun formatWhole() = with(HandleMoney) {
        assertEquals("0", formatWhole(""))
        assertEquals("123", formatWhole("123"))
        assertEquals("1,235.4", formatWhole("1235.4"))
        assertEquals("1,235", formatWhole("1235."))
        assertEquals("1,235", formatWhole("1235.0"))
    }
}
