package com.example.demo.chat.ollama_deepseek

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class OllamaResponse(
    val model: String,
    @JsonProperty("created_at")
    val createdAt: ZonedDateTime,
    val response: String
)
