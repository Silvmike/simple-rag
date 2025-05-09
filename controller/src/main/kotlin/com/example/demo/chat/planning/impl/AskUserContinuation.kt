package com.example.demo.chat.planning.impl

import com.example.demo.chat.planning.api.CommunicationContinuation
import com.example.demo.chat.planning.api.ExternalInputAwareContinuation
import com.example.demo.chat.planning.model.Communication
import com.example.demo.chat.planning.model.FunctionCall
import com.example.demo.chat.planning.model.FunctionStatus
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.TextNode

class AskUserContinuation(
    private val functionCall: FunctionCall,
    override val communication: Communication) : CommunicationContinuation, ExternalInputAwareContinuation {

    override fun hasNext(): Boolean = false

    override fun next(): CommunicationContinuation {
        throw NotImplementedError()
    }

    override val question: String
        get() = (functionCall.arguments[0].value as TextNode).textValue()

    override fun onExternalInput(input: JsonNode): CommunicationContinuation {

        val nextCommunication = Communication(
            query = communication.query,
            plan = communication.plan.map {
                if (it == functionCall) {
                    FunctionCall(
                        id = it.id,
                        order = it.order,
                        functionName = it.functionName,
                        arguments = it.arguments,
                        status = FunctionStatus.SUCCESS,
                        result = input
                    )
                } else {
                    it
                }
            },
            resolved = communication.resolved,
            finalResponse = communication.finalResponse
        )

        return SimpleContinuation(nextCommunication)
    }
}