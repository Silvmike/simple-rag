package com.example.demo.chat.planning.registry.api

import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode

interface CallableFunctionExecutor {

    fun execute(function: CallableFunction, arguments: Map<String, JsonNode>): JsonNode

}