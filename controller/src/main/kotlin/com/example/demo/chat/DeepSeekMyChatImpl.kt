package com.example.demo.chat

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.deepseek.api.DeepSeekClient
import com.example.demo.chat.deepseek.api.DeepSeekRequest
import com.example.demo.chat.deepseek.api.Message

class DeepSeekMyChatImpl(
    private val client: DeepSeekClient
) : MyChat {

    override fun exchange(message: String, options: Map<String, String>): String =
        client.exchange(
            DeepSeekRequest(
                model = "deepseek-chat",
                messages = listOf(
                    Message(
                        role = "user",
                        content = message
                    )
                ),
                temperature = options["temperature"]?.toFloat(),
                topP = options["top_p"]?.toFloat(),
                logprobs =
                    if (options.containsKey("top_logprobs")) true
                    else options["logprobs"]?.toBoolean(),
                topLogprobs = options["top_logprobs"]?.toInt(),
            )
        ).choices.firstOrNull()?.message?.content ?: throw IllegalStateException("No completion")

}