package com.example.demo.chat

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.deepseek.api.OllamaClient

class DeepSeekMyChatImpl(
    private val client: OllamaClient,
    private val model: String = "deepseek-r1:7b"
) : MyChat {

    override fun exchange(message: String): String =
        client.generate(
            model = model,
            prompt = message
        ).response

}