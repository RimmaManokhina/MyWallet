package com.github.cawboyroy.mywallet.di

import android.icu.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeTime @Inject constructor() : ProvideTime {

    private var time: Long = 0

    init {
        setTime(
            2025, Calendar.JUNE, 27,
            9, 30, 15
        )
    }

    override fun now() = time

    fun setTime(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minutes: Int,
        seconds: Int,
    ) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, seconds)

        time = calendar.timeInMillis
    }
}