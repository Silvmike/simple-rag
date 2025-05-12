package com.example.demo.chat.planning.registry.model

import com.fasterxml.jackson.databind.JsonNode

data class CallableFunction(
    val name: String,
    val description: String,
    val arguments: List<CallableArgument>,
    val responseModel: JsonNode
)
