package com.example.demo.chat.deepseek.api

data class DeepSeekRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false
)