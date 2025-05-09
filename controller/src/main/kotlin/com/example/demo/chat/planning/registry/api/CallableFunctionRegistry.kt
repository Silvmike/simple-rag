package com.example.demo.chat.planning.registry.api

import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.chat.planning.registry.model.Location
import java.util.*

interface CallableFunctionRegistry {

    fun lookup(functionName: String, location: Location): Optional<CallableFunction>

    fun lookup(functionName: String): List<CallableFunction>

    fun getAll(): List<CallableFunction>

    fun register(function: CallableFunction)

    fun unregister(function: CallableFunction) {
        throw NotImplementedError()
    }

}