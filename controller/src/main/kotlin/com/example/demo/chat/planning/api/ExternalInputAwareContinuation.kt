package com.example.demo.chat.planning.api

import com.fasterxml.jackson.databind.JsonNode

interface ExternalInputAwareContinuation {

    val question: String

    fun onExternalInput(input: JsonNode): CommunicationContinuation

}