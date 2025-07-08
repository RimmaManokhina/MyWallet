package com.github.cawboyroy.mywallet.add.presentation

import java.math.BigInteger

object HandleMoney {

    /**
     * @param raw is BigDecimal in String
     * @return formatted
     */
    fun ui(raw: String): String {
        if (raw == "" || raw == "0")
            return ""
        if (raw == ".")
            return "."

        if (raw.contains(".")) {
            val parts = raw.split(".")
            val left = parts[0]
            val right = parts[1]
            val leftUi = format(left)
            return "$leftUi.$right"
        } else {
            return format(raw)
        }
    }

    /**
     * @param ui is user input could be anything
     * @return BigDecimal in String
     */
    fun value(ui: String): String {
        if (ui.contains(".")) {
            val parts = ui.split(".")
            val left = filterOnlyDigits(parts[0]).trimStart('0')
            val finalLeft = left.ifEmpty { "0" }

            val right = filterOnlyDigits(parts[1])
            val last2: String = right.take(2)
            val finally = if (last2 == "00") "0" else last2
            return "$finalLeft.$finally"
        } else {
            return filterOnlyDigits(ui.trimStart('0'))
        }
    }

    private fun format(source: String): String {
        return source.reversed()
            .chunked(3)
            .joinToString(separator = ",")
            .reversed()
    }

    private fun formatMainPart(bigInteger: BigInteger): String {
        if (BigInteger.ZERO.equals(bigInteger))
            return ""
        val string = bigInteger.toString()
        return format(string)
    }

    private fun filterOnlyDigits(source: String): String {
        return source.filter { it.isDigit() }


    }

    fun formatWhole(currency: String, source: String): String {
        return ("$currency " + if (source.isEmpty() || source == "0")
            "0"
        else
            if (source.contains(".")) {
                val parts = source.split(".")
                if (parts[1].isEmpty() || parts[1] == "0")
                    formatMainPart(BigInteger(parts[0]))
                else
                    formatMainPart(BigInteger(parts[0])) + "." + parts[1]
            } else
                formatMainPart(BigInteger(source))).trim()
    }

    fun finalize(money: String): String {
        if (money.endsWith("."))
            return money.substring(0, money.indexOf("."))
        if (money.endsWith(".0"))
            return money.substring(0, money.indexOf(".0"))
        return money
    }
}