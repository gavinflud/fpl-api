package com.gavinflood.fpl.api.cache

/**
 * A basic interface for a cache.
 */
interface Cache {

    /**
     * The size of the cache.
     */
    val size: Int

    /**
     * Adds [value] to the cache, identified by [key]. If it already exists then it gets updated.
     */
    operator fun set(key: Any, value: Any)

    /**
     * Gets the value stored under [key] in the cache.
     */
    operator fun get(key: Any): Any?

    /**
     * Removes the value stored under [key] in the cache.
     */
    fun remove(key: Any): Any?

    /**
     * Clears all data from the cache.
     */
    fun clear()

}