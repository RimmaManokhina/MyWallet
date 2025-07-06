package com.github.cawboyroy.mywallet.add.presentation

import java.math.BigDecimal
import java.math.BigInteger

object HandleMoney {

    fun formatMainPart(bigInteger: BigInteger): String {
        if (BigInteger.ZERO.equals(bigInteger))
            return ""
        val string = bigInteger.toString()
        return string.reversed()
            .chunked(3)
            .joinToString(separator = ",")
            .reversed()
    }

    fun split(source: String): Pair<BigInteger, String> {
        val bigDecimal = BigDecimal(source)
        if (bigDecimal == BigDecimal.ZERO)
            return Pair(BigInteger.ZERO, "")
        val rest = source.split(".")[1]
        val bigInteger = bigDecimal.toBigInteger()
        return Pair(bigInteger, rest)
    }

    fun handleMainPart(source: String): String {
        val value = filterOnlyDigits(source)
        return if (value.isEmpty()) "0" else value
    }

    fun filterOnlyDigits(source: String): String {
        return source.filter { it.isDigit() }
    }

    fun handleCents(source: String): String {
        val digits = filterOnlyDigits(source)
        if (digits == "00")
            return "0"
        if (digits.length < 3)
            return digits
        return digits.take(2)
    }

    fun formatWhole(source: String): String {
        return if (source.isEmpty())
            "0"
        else
            if (source.contains(".")) {
                val parts = source.split(".")
                if (parts[1].isEmpty() || parts[1] == "0")
                    formatMainPart(BigInteger(parts[0]))
                else
                    formatMainPart(BigInteger(parts[0])) + "." + parts[1]
            } else
                formatMainPart(BigInteger(source))
    }
}