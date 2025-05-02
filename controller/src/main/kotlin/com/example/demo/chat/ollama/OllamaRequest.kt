package com.example.demo.chat.ollama

data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false
)