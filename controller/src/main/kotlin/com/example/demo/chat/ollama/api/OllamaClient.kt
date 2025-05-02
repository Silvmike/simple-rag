package com.example.demo.chat.ollama.api

import com.example.demo.chat.ollama.OllamaResponse

interface OllamaClient {

    fun generate(prompt: String, model: String): OllamaResponse
}