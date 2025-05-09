package com.example.demo.chat.planning.impl

import com.example.demo.chat.planning.api.CommunicationContinuation
import com.example.demo.chat.planning.api.CommunicationExecutor
import com.example.demo.chat.planning.api.ExternalInputAwareContinuation
import com.example.demo.chat.planning.model.Argument
import com.example.demo.chat.planning.model.Communication
import com.example.demo.chat.planning.model.FunctionCall
import com.example.demo.chat.planning.model.FunctionStatus
import com.fasterxml.jackson.databind.node.TextNode
import java.util.*

/**
 * TODO the idea is to break down original user query into library function calls.
 * TODO Then evaluate them one by one.
 * TODO Then ask LLM to optimize plan or answer the question.
 * TODO library can consist internal functions as well as known MCP tools calls.
 *
 * This is just a PoC in terminal.
 */
fun main() {
    var order = 1
    var current = Communication(
        query = "Sum two integers then add another one",
        plan = listOf(
            FunctionCall(
                id = "@1",
                functionName = "ask_user",
                arguments = listOf(Argument(name = "query", TextNode("Enter number: "))),
                order = order++,
                status = FunctionStatus.TODO
            ),
            FunctionCall(
                id = "@2",
                functionName = "ask_user",
                arguments = listOf(Argument(name = "query", TextNode("Enter number: "))),
                order = order++,
                status = FunctionStatus.TODO
            ),
            FunctionCall(
                id = "@3",
                functionName = "sum",
                arguments = listOf(
                    Argument(name = "value", TextNode("@1")),
                    Argument(name = "value", TextNode("@2"))
                ),
                order = order++,
                status = FunctionStatus.TODO
            ),
            FunctionCall(
                id = "@4",
                functionName = "ask_user",
                arguments = listOf(Argument(name = "query", TextNode("Enter number: "))),
                order = order++,
                status = FunctionStatus.TODO
            ),
            FunctionCall(
                id = "@5",
                functionName = "sum",
                arguments = listOf(
                    Argument(name = "value", TextNode("@3")),
                    Argument(name = "value", TextNode("@4"))
                ),
                order = order,
                status = FunctionStatus.TODO
            )
        ),
    )

    val executor = Executor()
    val inputScanner = Scanner(System.`in`)

    var continuation = executor.execute(current)
    while (!current.resolved) {

        continuation = iterateContinuations(continuation)

        if (continuation is ExternalInputAwareContinuation) {

            println(continuation.question)
            val response = inputScanner.nextLine()
            continuation = continuation.onExternalInput(TextNode(response))
        } else {
            println("Executing next iteration")
            continuation = executor.execute(continuation.communication)
        }

        current = continuation.communication
    }

    println(current.finalResponse)

}

private fun iterateContinuations(continuation: CommunicationContinuation): CommunicationContinuation {
    var currentContinuation = continuation
    while (currentContinuation.hasNext()) {
        currentContinuation = currentContinuation.next()
    }
    return currentContinuation
}

class Executor : CommunicationExecutor {

    override fun execute(communication: Communication): CommunicationContinuation {
        val nextFunctionCall = communication.plan.asSequence()
            .sortedBy { it.order }
            .filter {
                it.status == FunctionStatus.TODO
            }.firstOrNull()

        if (nextFunctionCall?.functionName == "sum") {

            val planMap = communication.plan.asSequence()
                .filter { it.status == FunctionStatus.SUCCESS }
                .groupBy { it.id }

            val result = nextFunctionCall.arguments.sumOf {
                resolveArgument(planMap, it)
            }.toString()

            val nextCommunication = Communication(
                query = communication.query,
                plan = communication.plan.map {
                    if (nextFunctionCall == it) {
                        FunctionCall(
                            id = it.id,
                            status = FunctionStatus.SUCCESS,
                            result = TextNode(result),
                            functionName = it.functionName,
                            arguments = it.arguments,
                            order = it.order
                        )
                    } else {
                        it
                    }
                },
                finalResponse = communication.finalResponse,
                resolved = communication.resolved
            )

            return SimpleContinuation(nextCommunication)
        }

        if (nextFunctionCall?.functionName == "ask_user") {
            return AskUserContinuation(nextFunctionCall, communication)
        }

        if (nextFunctionCall == null) {
            return SimpleContinuation(
                Communication(
                    query = communication.query,
                    plan = communication.plan,
                    finalResponse = communication.plan.last().result!!.textValue(),
                    resolved = true,
                )
            )
        }

        return SimpleContinuation(communication)
    }

    private fun resolveArgument(planMap: Map<String, List<FunctionCall>>, argument: Argument): Int {
        val value = argument.value!!.textValue()
        return if (value.startsWith("@")) {
            planMap[value]?.get(0)?.result?.textValue()!!.toInt()
        } else {
            value.toInt()
        }
    }

}
