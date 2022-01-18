package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.mapper.Mapper

/**
 * Handles functionality for a specific domain (e.g. fixtures, managers, etc.).
 */
abstract class Handler {

    /**
     * Used to map raw response data to domain objects.
     */
    internal val mapper = Mapper()

}