package com.example.demo.config.query.vector

import com.example.demo.embedding.EmbedClientImpl
import com.example.demo.embedding.MyEmbeddingModel
import com.example.demo.embedding.api.EmbedClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class EmbeddingModelClientConfig {

    @Bean
    fun embeddingModel(embedClient: EmbedClient) = MyEmbeddingModel(embedClient)

    @Bean
    fun embedClient(@Qualifier(EMBEDDING_HTTP_CLIENT) httpClient: HttpClient) =
        EmbedClientImpl(httpClient) {
            "http://localhost:8000/embed"
        }

    @Bean(EMBEDDING_HTTP_CLIENT)
    fun embedHttpClient(): HttpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_1_1)
            .build()

    companion object {
        private const val EMBEDDING_HTTP_CLIENT = "embedHttpClient"
    }
}