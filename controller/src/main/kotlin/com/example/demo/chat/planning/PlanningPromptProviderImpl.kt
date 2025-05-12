package com.example.demo.chat.planning

import com.example.demo.chat.planning.api.PlanningPromptProvider
import com.example.demo.chat.planning.model.FunctionCall
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.util.template.ResourceTemplateProvider
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class PlanningPromptProviderImpl : PlanningPromptProvider {

    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())

    override fun provide(
        tools: List<CallableFunction>,
        plan: List<FunctionCall>,
        query: String,
        response: JsonNode?
    ): String {

        val instructionsTemplate = ResourceTemplateProvider("planning/instruction.txt")

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            PlanningPrompt(
                instruction = instructionsTemplate.get().text(),
                plan = plan,
                functions = tools,
                query = query,
                response = response
            )
        )
    }
}

private data class PlanningPrompt(
    val instruction: String,
    val functions: List<CallableFunction>,
    val plan: List<FunctionCall>,
    val query: String,
    val response: JsonNode?,
    val resolved: Boolean = false
)