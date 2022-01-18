package com.gavinflood.fpl.api.cache

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpirableCacheTest {

    @Test
    fun `cache size is zero when initialized`() = assertEquals(ExpirableCache().size, 0)

    @Test
    fun `item is added to the cache`() {
        val cache = ExpirableCache()
        val key = "tmp"
        val value = 10

        cache[key] = value
        assertEquals(value, cache[key])
    }

    @Test
    fun `item is updated in cache`() {
        val cache = ExpirableCache()
        val key = "tmp"
        val value = 10
        val newValue = 20

        cache[key] = value
        assertEquals(value, cache[key])

        cache[key] = newValue
        assertEquals(newValue, cache[key])
    }

    @Test
    fun `item is removed from cache`() {
        val cache = ExpirableCache()
        val key = "tmp"
        val value = 10

        cache[key] = value
        assertEquals(value, cache[key])

        cache.remove(key)
        assertNull(cache[key])
    }

    @Test
    fun `all items are removed from cache`() {
        val cache = ExpirableCache()
        val key1 = "tmp1"
        val value1 = 10
        val key2 = "tmp2"
        val value2 = 20

        cache[key1] = value1
        assertEquals(value1, cache[key1])

        cache[key2] = value2
        assertEquals(value2, cache[key2])

        cache.clear()
        assertNull(cache[key1])
        assertNull(cache[key2])
    }

    @Test
    fun `cache is cleared after flush interval has passed`() {
        val cache = ExpirableCache(TimeUnit.MILLISECONDS.toMillis(10))
        val key = "tmp1"
        val value = 10

        cache[key] = value
        assertEquals(value, cache[key])

        Thread.sleep(TimeUnit.MILLISECONDS.toMillis(20))
        assertEquals(null, cache[key])
    }

}