package com.example.demo.chat.planning.demo.functions

import com.example.demo.chat.planning.registry.api.CallableFunctionExecutor
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils
import org.springframework.core.io.Resource
import java.nio.charset.Charset

class FileReadingFunction(
    private val resource: Resource
) : CallableFunctionExecutor {

    private val objectMapper = ObjectMapper()

    override fun execute(function: CallableFunction, arguments: Map<String, JsonNode>): JsonNode =
        objectMapper.readTree(
            resource.inputStream.use {
                IOUtils.toString(it, Charset.defaultCharset())
            }
        )

}