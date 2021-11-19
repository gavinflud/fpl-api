package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.mapper.Mapper

abstract class Handler {
    
    protected val mapper = Mapper()

}