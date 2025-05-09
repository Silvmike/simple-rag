package com.example.demo.chat.planning.api

import com.example.demo.chat.planning.model.Communication

interface CommunicationExecutor {

    fun execute(communication: Communication): CommunicationContinuation

}