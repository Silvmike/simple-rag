package com.example.demo.config.query

import com.example.demo.reranker.RerankerClientImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration

private const val RERANKER_HTTP_CLIENT = "rerankerHttpClient"

@Configuration
class RerankerClientConfig {

    @Bean
    fun rerankerClient(@Qualifier(RERANKER_HTTP_CLIENT) httpClient: HttpClient) =
        RerankerClientImpl(httpClient) {
            "http://localhost:8001/rerank"
        }

    @Bean(RERANKER_HTTP_CLIENT)
    fun rerankerHttpClient(): HttpClient =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .version(HttpClient.Version.HTTP_1_1)
            .build()
}