package com.gavinflood.fpl.api.cache

/**
 * A basic interface for a cache.
 */
interface Cache {

    val size: Int

    operator fun set(key: Any, value: Any)

    operator fun get(key: Any): Any?

    fun remove(key: Any): Any?

    fun clear()

}