package com.example.demo.config

import com.example.demo.embedding.MyEmbeddingModel
import com.example.demo.embedding.StubEmbeddingModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class EmbeddingModelClientConfig {

    @Bean
    fun embeddingModel(httpClient: HttpClient) = MyEmbeddingModel(
        httpClient
    ) {
        "http://localhost:8000/embed"
    }

    @Bean
    fun httpClient(): HttpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_1_1)
            .build()
}