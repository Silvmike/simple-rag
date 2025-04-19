package com.example.demo.chat

import com.example.demo.chat.api.ModelSelector
import com.example.demo.chat.giga.api.GigaChatClient
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class ModelSelectorImpl(
    private val gigaChatClient: GigaChatClient,
    private val preferredModels: Set<String> = setOf("GigaChat")
) : ModelSelector {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val modelCache = ConcurrentHashMap<String, String>()

    override fun selectModel(): String =
        modelCache.computeIfAbsent("model") {
            gigaChatClient.getModels().data!!.let { models ->
                val suitable = models
                    .filter { model -> model.type == "chat" }
                    .mapNotNull { model -> model.id }

                suitable.firstOrNull { modelName ->
                    preferredModels.contains(modelName)
                } ?: suitable[0]
            }.apply {
                logger.info("[GigaChat] Selected model: $this")
            }
        }
}