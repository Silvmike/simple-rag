package com.example.demo.chat.deepseek.api

import com.example.demo.chat.deepseek.OllamaResponse

interface OllamaClient {

    fun generate(prompt: String, model: String): OllamaResponse
}