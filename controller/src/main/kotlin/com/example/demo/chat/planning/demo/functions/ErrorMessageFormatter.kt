package com.example.demo.chat.planning.demo.functions

import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode

fun interface ErrorMessageFormatter {

    fun format(
        function: CallableFunction,
        arguments: Map<String, JsonNode>,
        exception: Exception
    ): String

}