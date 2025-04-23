package com.example.demo.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Suppliers

private val mapper = ObjectMapper()

class LazyStringifier(
    obj: Any
) {

    private val lazyString = Suppliers.memoize {
        mapper.writeValueAsString(obj)
    }

    override fun toString(): String =
        lazyString.get()

}