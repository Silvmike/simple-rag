package com.example.demo.chat.planning

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.planning.api.PlanningPromptProvider
import com.example.demo.chat.planning.model.FunctionCall
import com.example.demo.chat.planning.model.FunctionStatus
import com.example.demo.chat.planning.model.PlannerResponse
import com.example.demo.chat.planning.registry.api.CallableFunctionExecutorRegistry
import com.example.demo.chat.planning.registry.api.CallableFunctionRegistry
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class Planner(
    private val myChat: MyChat,
    private val callableFunctionRegistry: CallableFunctionRegistry,
    private val callableFunctionExecutorRegistry: CallableFunctionExecutorRegistry,
    private val planningPromptProvider: PlanningPromptProvider,
    private val maxIterations: Int = 10
) {

    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())

    fun plan(query: String, temperature: Float = 0.3f): String {

        val tools = callableFunctionRegistry.getAll()

        var currentPlan: List<FunctionCall> = emptyList()
        var currentResponse: JsonNode? = null
        var forceRespond = false

        for (i in 1..maxIterations) {


            val prompt = planningPromptProvider.provide(
                tools = tools,
                plan = currentPlan,
                query = query,
                response = null,
                forceRespond = forceRespond
            )

            println("NEW PROMPT: $prompt")

            val response = myChat.exchange(
                message = prompt,
                options = mapOf(
                    "temperature" to temperature.toString()
                )
            ).replace("```json", "").replace("```", "")
            println("LLM RESPONSE: $response")

            val plannerResponse = mapper.readValue(response, PlannerResponse::class.java)

            if (plannerResponse.resolved) {
                return plannerResponse.response!!.textValue()
            }

            currentResponse = plannerResponse.response

            val oldPlan = currentPlan
            currentPlan = evaluatePlan(mergePlans(currentPlan, plannerResponse.plan))
            forceRespond = oldPlan == currentPlan

        }

        return "Извините, я пока только учусь."
    }

    private fun mergePlans(currentPlan: List<FunctionCall>, plan: List<FunctionCall>): List<FunctionCall> {
        val currentPlanMap = mutableMapOf<String, FunctionCall>().apply {
            putAll(currentPlan.associateBy { it.id })
        }
        plan.filter { currentPlanMap[it.id]?.status != "SUCCESS" }.forEach {
            currentPlanMap[it.id] = it
        }
        return currentPlanMap.values.toList()
    }

    private fun evaluatePlan(plan: List<FunctionCall>): List<FunctionCall> =
        plan.map { call ->

            if (call.result != null && call.status != "NEW") call else {

                val function = callableFunctionRegistry.lookup(call.function)[0]
                val executor = callableFunctionExecutorRegistry.lookup(function)
                var status = FunctionStatus.SUCCESS.name
                var statusDescription: String? = null
                var result: JsonNode? = null
                try {
                    result = executor.get().execute(
                        function = function,
                        arguments = mutableMapOf<String, JsonNode>().apply {
                            call.arguments.forEach {
                                putAll(it)
                            }
                        }
                    )
                } catch (e: Exception) {
                    status = FunctionStatus.FAILURE.name
                    statusDescription = e.message
                }
                FunctionCall(
                    id = call.id,
                    function = call.function,
                    result = result,
                    status = status,
                    statusDescription = statusDescription,
                    arguments = call.arguments,
                )
            }
        }

}