package com.example.demo.chat.planning.demo.functions

import com.example.demo.chat.planning.registry.api.CallableFunctionExecutor
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import java.nio.charset.Charset

class MultiFileReadingFunction(
    private val argumentNames: List<String>,
    private val sourceFileNameFormatter: SourceFileNameFormatter,
    private val errorMessageFormatter: ErrorMessageFormatter
) : CallableFunctionExecutor {

    private val objectMapper = ObjectMapper()

    override fun execute(function: CallableFunction, arguments: Map<String, JsonNode>): JsonNode {
        try {

            val parsedArguments = argumentNames.associateWith {
                arguments[it]?.asText()?.lowercase()
            }

            val resource = ClassPathResource(
                sourceFileNameFormatter.format(parsedArguments)
            )
            return objectMapper.readTree(
                resource.inputStream.use {
                    IOUtils.toString(it, Charset.defaultCharset())
                }
            )
        } catch (e: Exception) {
            throw RuntimeException(errorMessageFormatter.format(function, arguments, e))
        }
    }

}