package com.example.demo.chat.planning.api

import com.example.demo.chat.planning.model.Communication

interface CommunicationContinuation : Iterator<CommunicationContinuation> {

    val communication: Communication

}