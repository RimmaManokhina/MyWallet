package com.github.cawboyroy.mywallet

import junit.framework.TestCase.assertEquals
import org.junit.Test

class MonthsUiSimpleTest {

    @Test
    fun testExpandedOne() {
        val mapper = Mapper()
        val actual: List<Ui> = mapper.map(
            collapsedIds = setOf<Int>(),
            source = listOf<Ui.Record>(
                Ui.Record(1, 1.0),
                Ui.Record(1, 2.0),
            )
        )
        val expected: List<Ui> = listOf(
            Ui.Record(1, 1.0),
            Ui.Record(1, 2.0),
            Ui.Header(1, 3.0, false)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testCollapsedOne() {
        val mapper = Mapper()
        val actual: List<Ui> = mapper.map(
            collapsedIds = setOf<Int>(1),
            source = listOf<Ui.Record>(
                Ui.Record(1, 1.0),
                Ui.Record(1, 2.0),
            )
        )
        val expected: List<Ui> = listOf(
            Ui.Header(1, 3.0, true)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testCollapsedFirstExpandedSecond() {
        val mapper = Mapper()
        val actual: List<Ui> = mapper.map(
            collapsedIds = setOf<Int>(1),
            source = listOf<Ui.Record>(
                Ui.Record(1, 1.0),
                Ui.Record(1, 2.0),
                Ui.Record(2, 10.0),
            )
        )
        val expected: List<Ui> = listOf(
            Ui.Header(1, 3.0, true),
            Ui.Record(2, 10.0),
            Ui.Header(2, 10.0, false)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun testCollapsedBoth() {
        val mapper = Mapper()
        val actual: List<Ui> = mapper.map(
            collapsedIds = setOf<Int>(1, 2),
            source = listOf<Ui.Record>(
                Ui.Record(1, 1.0),
                Ui.Record(1, 2.0),
                Ui.Record(2, 10.0),
            )
        )
        val expected: List<Ui> = listOf(
            Ui.Header(1, 3.0, true),
            Ui.Header(2, 10.0, true)
        )
        assertEquals(expected, actual)
    }

}

interface Ui {

    data class Record(val day: Int, val money: Double) : Ui
    data class Header(val day: Int, val money: Double, val collapsed: Boolean) : Ui
}

class Mapper {
    fun map(collapsedIds: Set<Int>, source: List<Ui.Record>): List<Ui> {
        val result = mutableListOf<Ui>()

        var currentDaySum = 0.0
        var currentDayId = -1

        source.forEachIndexed { index, record ->
            if (currentDayId == -1) {
                currentDayId = record.day
            } else if (record.day != currentDayId) {
                result.add(
                    Ui.Header(
                        currentDayId,
                        currentDaySum,
                        collapsedIds.contains(currentDayId)
                    )
                )
                currentDayId = record.day
                currentDaySum = 0.0
            }

            val isCurrentRecordDayCollapsed = collapsedIds.contains(record.day)

            if (!isCurrentRecordDayCollapsed) {
                result.add(record)
            }
            currentDaySum += record.money

            if (index == source.size - 1) {
                result.add(
                    Ui.Header(
                        currentDayId,
                        currentDaySum,
                        collapsedIds.contains(currentDayId)
                    )
                )
            }
        }

        return result
    }

}