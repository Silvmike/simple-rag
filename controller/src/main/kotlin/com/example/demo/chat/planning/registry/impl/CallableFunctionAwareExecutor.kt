package com.example.demo.chat.planning.registry.impl

import com.example.demo.chat.planning.registry.api.CallableFunctionExecutor
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode

class CallableFunctionAwareExecutor(
    val callableFunction: CallableFunction,
    private val executor: CallableFunctionExecutor
) : CallableFunctionExecutor {

    override fun execute(function: CallableFunction, arguments: Map<String, JsonNode>): JsonNode =
        executor.execute(callableFunction, arguments)

}