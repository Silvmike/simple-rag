package com.example.demo.config

import com.example.demo.embedding.EmbedClient
import com.example.demo.embedding.EmbedClientImpl
import com.example.demo.embedding.MyEmbeddingModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class EmbeddingModelClientConfig {

    @Bean
    fun embeddingModel(embedClient: EmbedClient) = MyEmbeddingModel(embedClient)

    @Bean
    fun embedClient(httpClient: HttpClient) =
        EmbedClientImpl(httpClient) {
            "http://localhost:8000/embed"
        }

    @Bean
    fun httpClient(): HttpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_1_1)
            .build()
}