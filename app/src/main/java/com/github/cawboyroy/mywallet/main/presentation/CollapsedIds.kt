package com.github.cawboyroy.mywallet.main.presentation

import java.io.Serializable

data class CollapsedIds(private val set: Set<Int> = emptySet()) : Serializable {

    fun add(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.add(day) }.toSet())
    }

    fun remove(day: Int): CollapsedIds {
        return CollapsedIds(set.toMutableSet().also { it.remove(day) }.toSet())
    }

    fun value(): Set<Int> = set

    fun collapseAll(): CollapsedIds {
        return CollapsedIds((1..31).toSet())
    }

    fun expandAll(): CollapsedIds {
        return CollapsedIds(emptySet())
    }
}