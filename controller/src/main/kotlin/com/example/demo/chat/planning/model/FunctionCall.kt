package com.example.demo.chat.planning.model

import com.fasterxml.jackson.databind.JsonNode

data class FunctionCall(
    val order: Int,
    val id: String,
    val functionName: String,
    var arguments: List<Argument> = emptyList(),
    val status: FunctionStatus,
    var result: JsonNode? = null
)