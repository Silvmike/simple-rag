package com.example.demo.chat.planning.model

import com.fasterxml.jackson.databind.JsonNode

data class FunctionCall(
    var id: String,
    var function: String,
    var arguments: List<Map<String, JsonNode>> = listOf(),
    var status: String,
    var statusDescription: String? = null,
    var result: JsonNode? = null
)