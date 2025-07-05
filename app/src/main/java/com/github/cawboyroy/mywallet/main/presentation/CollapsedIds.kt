package com.github.cawboyroy.mywallet.main.presentation

import java.io.Serializable

data class CollapsedIds(private val set: Set<Int>) : Serializable {

    fun clear(): CollapsedIds {
        return CollapsedIds(emptySet())
    }

    fun add(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.add(day) }.toSet())
    }

    fun remove(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.remove(day) }.toSet())
    }

    fun value(): Set<Int> = set
}