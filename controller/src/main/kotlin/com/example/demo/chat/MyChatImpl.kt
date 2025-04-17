package com.example.demo.chat

import com.example.demo.chat.api.ModelSelector
import com.example.demo.chat.api.MyChat
import com.example.demo.chat.giga.api.GigaChatClient
import com.example.demo.chat.giga.model.Chat
import com.example.demo.chat.giga.model.Message

class MyChatImpl(
    private val gigaChatClient: GigaChatClient,
    private val modelSelector: ModelSelector
) : MyChat {

    override fun exchange(message: String): String {
        val model = modelSelector.selectModel()
        val chat = Chat(
            model = model,
            messages = listOf(Message(content = message, role = Message.Role.user)),
            repetitionPenalty = 1.0f
        )
        return gigaChatClient.getCompletions(chat).choices!!
            .asSequence()
            .map { choice -> choice.message!!.content }
            .filterNotNull()
            .first()
    }
}