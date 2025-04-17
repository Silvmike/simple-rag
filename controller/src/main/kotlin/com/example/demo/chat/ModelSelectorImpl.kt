package com.example.demo.chat

import com.example.demo.chat.api.ModelSelector
import com.example.demo.chat.giga.api.GigaChatClient
import java.util.concurrent.ConcurrentHashMap

class ModelSelectorImpl(
    private val gigaChatClient: GigaChatClient
) : ModelSelector {

    private val modelCache = ConcurrentHashMap<String, String>()

    override fun selectModel(): String =
        modelCache.computeIfAbsent("model") {
            gigaChatClient.getModels().data!!.asSequence()
                .filter { it.type == "chat" }
                .map { it.id }
                .filterNotNull()
                .first()
        }
}