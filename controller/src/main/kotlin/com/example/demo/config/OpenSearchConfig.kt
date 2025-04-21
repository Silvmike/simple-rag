package com.example.demo.config

import com.example.demo.opensearch.UnsafeOpenSearchClientFactory
import com.example.demo.startup.PrepareOpenSearchIndex
import org.apache.hc.core5.http.HttpHost
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.transport.OpenSearchTransport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenSearchConfig {

    @Bean
    fun prepareIndex(client: OpenSearchClient) = PrepareOpenSearchIndex(client)

    @Bean
    fun opensearchClient(transport: OpenSearchTransport) =
        OpenSearchClient(transport)

    @Bean
    fun opensearchTransport(): OpenSearchTransport =
        UnsafeOpenSearchClientFactory.create(
            HttpHost("localhost", 9200)
        )
}