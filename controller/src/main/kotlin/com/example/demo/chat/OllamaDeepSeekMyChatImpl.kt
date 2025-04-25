package com.example.demo.chat

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.ollama_deepseek.api.OllamaClient

class OllamaDeepSeekMyChatImpl(
    private val client: OllamaClient
) : MyChat {

    override fun exchange(message: String): String =
        client.generate(
            model = "deepseek-r1:7b",
            prompt = message
        ).response

}