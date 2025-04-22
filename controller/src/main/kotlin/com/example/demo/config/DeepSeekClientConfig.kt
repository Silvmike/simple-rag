package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.chat.DeepSeekMyChatImpl
import com.example.demo.chat.deepseek.OllamaClientImpl
import com.example.demo.chat.deepseek.api.OllamaClient
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Duration

@Profile(Profiles.DEEPSEEK)
@Configuration
class DeepSeekClientConfig {

    @Bean
    fun deepSeekClient() = OllamaClientImpl(
        OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(600))
            .readTimeout(Duration.ofSeconds(600))
            .build(),
        "http://localhost:11434",
    )

    @Bean
    fun myChat(
        ollamaClient: OllamaClient,
    ) = DeepSeekMyChatImpl(ollamaClient)

}