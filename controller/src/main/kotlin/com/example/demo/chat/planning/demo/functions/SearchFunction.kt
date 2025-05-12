package com.example.demo.chat.planning.demo.functions

import com.example.demo.chat.planning.registry.api.CallableFunctionExecutor
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.search_engines.api.SearchEngineClient
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class SearchFunction(
    private val searchEngineClient: SearchEngineClient
) : CallableFunctionExecutor {

    private val objectMapper = ObjectMapper()

    override fun execute(function: CallableFunction, arguments: Map<String, JsonNode>): JsonNode =
        objectMapper.valueToTree(
            searchEngineClient.query(arguments["query"]!!.textValue())
        )
}