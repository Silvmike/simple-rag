package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.chat.OllamaMyChatImpl
import com.example.demo.chat.ollama.OllamaClientImpl
import com.example.demo.chat.ollama.api.OllamaClient
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Duration

@Profile(Profiles.OLLAMA_CHAT)
@Configuration
class OllamaDeepSeekClientConfig {

    @Bean
    fun ollamaDeepSeekClient() = OllamaClientImpl(
        OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(600))
            .readTimeout(Duration.ofSeconds(600))
            .build(),
        "http://localhost:11434",
    )

    @Bean
    fun myChat(
        ollamaClient: OllamaClient,
        @Value("\${ollama.model}")
        model: String
    ) = OllamaMyChatImpl(ollamaClient, model)

}