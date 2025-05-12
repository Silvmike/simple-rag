package com.example.demo.chat.planning.model

import com.fasterxml.jackson.databind.JsonNode

data class PlannerResponse(
    var plan: List<FunctionCall>,
    var response: JsonNode?,
    var resolved: Boolean
)