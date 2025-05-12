package com.example.demo.chat.planning.registry.impl

import com.example.demo.chat.planning.registry.api.CallableFunctionRegistry
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.chat.planning.registry.model.Location
import java.util.*

class SimpleCallableFunctionRegistry : CallableFunctionRegistry {

    private val registry = mutableMapOf<String, CallableFunction>()

    override fun lookup(functionName: String, location: Location): Optional<CallableFunction> {
        throw UnsupportedOperationException()
    }

    override fun lookup(functionName: String): List<CallableFunction> =
        if (registry.containsKey(functionName)) {
            listOf(registry[functionName]!!)
        } else {
            emptyList()
        }

    override fun getAll(): List<CallableFunction> = registry.values.toList()

    override fun register(function: CallableFunction) {
        registry[function.name] = function
    }
}