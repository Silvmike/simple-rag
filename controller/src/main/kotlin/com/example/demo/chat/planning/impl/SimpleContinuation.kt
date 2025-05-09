package com.example.demo.chat.planning.impl

import com.example.demo.chat.planning.api.CommunicationContinuation
import com.example.demo.chat.planning.model.Communication

class SimpleContinuation(override val communication: Communication) : CommunicationContinuation {

    override fun hasNext(): Boolean = false

    override fun next(): CommunicationContinuation {
        throw NotImplementedError()
    }
}