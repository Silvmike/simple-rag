package com.example.demo.config

import com.example.demo.embedding.StubEmbeddingModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmbeddingModelClientConfig {

    @Bean
    fun embeddingModel() = StubEmbeddingModel()
}