package com.gavinflood.fpl.api.cache

import java.util.concurrent.TimeUnit

/**
 * A simple cache implementation where the cache expires once the [flushInterval] has passed.
 */
class ExpirableCache(
    private val flushInterval: Long = TimeUnit.SECONDS.toMillis(30)
) : Cache {

    private var timeLastFlushed = System.nanoTime()
    private val cache = HashMap<Any, Any>()

    override val size: Int
        get() = cache.size

    override fun set(key: Any, value: Any) {
        cache[key] = value
    }

    override fun get(key: Any): Any? {
        recycle()
        return cache[key]
    }

    override fun remove(key: Any): Any? {
        recycle()
        return cache.remove(key)
    }

    override fun clear() {
        cache.clear()
    }

    /**
     * Clear the cache if the [flushInterval] has passed.
     */
    private fun recycle() {
        if (System.nanoTime() - timeLastFlushed >= TimeUnit.MILLISECONDS.toNanos(flushInterval)) {
            clear()
            timeLastFlushed = System.nanoTime()
        }
    }

}