package com.example.demo.config

import com.example.demo.chat.DeepSeekMyChatImpl
import com.example.demo.chat.EnvironmentTokenProvider
import com.example.demo.chat.deepseek.DeepSeekClientImpl
import com.example.demo.chat.deepseek.api.DeepSeekClient
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@ConditionalOnProperty(
    name = ["options.model.deep-seek.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Configuration
class DeepSeekClientConfig {

    @Bean
    fun deepSeekClient() = DeepSeekClientImpl(
        httpClient = OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(600))
            .readTimeout(Duration.ofSeconds(600))
            .build(),
        tokenProvider = EnvironmentTokenProvider("DEEP_SEEK_API_KEY")
    )

    @Bean
    fun myChat(
        deepSeekClient: DeepSeekClient
    ) = DeepSeekMyChatImpl(deepSeekClient)

}