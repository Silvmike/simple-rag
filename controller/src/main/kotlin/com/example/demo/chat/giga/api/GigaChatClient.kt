package com.example.demo.chat.giga.api

import com.example.demo.chat.giga.model.Chat
import com.example.demo.chat.giga.model.ChatCompletion
import com.example.demo.chat.giga.model.Models

interface GigaChatClient {

    fun getCompletions(chat: Chat): ChatCompletion

    fun getModels(): Models
}