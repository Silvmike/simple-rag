package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.chat.OllamaDeepSeekMyChatImpl
import com.example.demo.chat.ollama_deepseek.OllamaClientImpl
import com.example.demo.chat.ollama_deepseek.api.OllamaClient
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Duration

@Profile(Profiles.OLLAMA_DEEPSEEK)
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
    ) = OllamaDeepSeekMyChatImpl(ollamaClient)

}