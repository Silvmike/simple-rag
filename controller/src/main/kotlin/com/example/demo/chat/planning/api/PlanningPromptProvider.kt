package com.example.demo.chat.planning.api

import com.example.demo.chat.planning.model.FunctionCall
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode

interface PlanningPromptProvider {

    fun provide(
        tools: List<CallableFunction>,
        plan: List<FunctionCall>,
        query: String,
        response: JsonNode?,
        forceRespond: Boolean
    ): String

}