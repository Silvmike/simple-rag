package com.example.demo.config

import org.springframework.ai.vectorstore.qdrant.autoconfigure.QdrantVectorStoreAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration

@EnableAutoConfiguration(
    exclude = [
        QdrantVectorStoreAutoConfiguration::class
    ]
)
@ConditionalOnProperty(
    name = ["options.vector-search.enabled"],
    havingValue = "false",
    matchIfMissing = false
)
@Configuration
class DisableQdrantConfiguration {
}