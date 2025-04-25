package com.example.demo.chat

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.deepseek.api.DeepSeekClient
import com.example.demo.chat.deepseek.api.DeepSeekRequest
import com.example.demo.chat.deepseek.api.Message
import com.example.demo.chat.ollama_deepseek.api.OllamaClient

class DeepSeekMyChatImpl(
    private val client: DeepSeekClient
) : MyChat {

    override fun exchange(message: String): String =
        client.exchange(
            DeepSeekRequest(
                model = "deepseek-reasoner",
                messages = listOf(
                    Message(
                        role = "user",
                        content = message
                    )
                )
            )
        ).choices.firstOrNull()?.message?.content ?: throw IllegalStateException("No completion")

}