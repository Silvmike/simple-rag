package com.example.demo.chat.ollama_deepseek.api

import com.example.demo.chat.ollama_deepseek.OllamaResponse

interface OllamaClient {

    fun generate(prompt: String, model: String): OllamaResponse
}