package com.example.demo.chat.deepseek.api

import com.fasterxml.jackson.annotation.JsonProperty

data class DeepSeekRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false,
    @JsonProperty(value = "max_tokens")
    val maxTokens: Int? = null,
    val temperature: Float? = null,
    @JsonProperty(value = "top_p")
    val topP: Float? = null,
    @JsonProperty(value = "top_logprobs")
    val topLogprobs: Int? = null,
    @JsonProperty(value = "logprobs")
    val logprobs: Boolean? = null
)