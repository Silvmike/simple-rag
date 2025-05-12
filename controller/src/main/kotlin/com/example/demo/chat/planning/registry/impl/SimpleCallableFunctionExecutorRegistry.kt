package com.example.demo.chat.planning.registry.impl

import com.example.demo.chat.planning.registry.api.CallableFunctionExecutor
import com.example.demo.chat.planning.registry.api.CallableFunctionExecutorRegistry
import com.example.demo.chat.planning.registry.model.CallableFunction
import java.util.*

class SimpleCallableFunctionExecutorRegistry : CallableFunctionExecutorRegistry {

    private var registry = mutableMapOf<String, CallableFunctionExecutor>()

    override fun lookup(function: CallableFunction): Optional<CallableFunctionExecutor> =
        Optional.ofNullable(registry[function.name])

    fun register(executor: CallableFunctionExecutor, function: CallableFunction) {
        registry[function.name] = executor
    }
}