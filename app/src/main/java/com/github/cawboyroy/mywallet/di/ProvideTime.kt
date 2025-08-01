package com.github.cawboyroy.mywallet.di

import javax.inject.Inject

interface ProvideTime {

    fun now(): Long

    class Base @Inject constructor() : ProvideTime {

        override fun now() = System.currentTimeMillis()
    }
}