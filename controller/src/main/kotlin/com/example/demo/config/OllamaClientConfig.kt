package com.example.demo.config

import com.example.demo.chat.OllamaMyChatImpl
import com.example.demo.chat.ollama.OllamaClientImpl
import com.example.demo.chat.ollama.api.OllamaClient
import com.example.demo.config.properties.Options
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@ConditionalOnProperty(
    name = ["options.model.ollama.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Configuration
class OllamaClientConfig {

    @Bean
    fun ollamaClient() = OllamaClientImpl(
        OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(600))
            .readTimeout(Duration.ofSeconds(600))
            .build(),
        "http://localhost:11434",
    )

    @Bean
    fun myChat(
        ollamaClient: OllamaClient,
        options: Options
    ) = OllamaMyChatImpl(ollamaClient, options.model.ollama.name)

}