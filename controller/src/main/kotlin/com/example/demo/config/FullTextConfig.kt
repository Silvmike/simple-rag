package com.example.demo.config

import com.example.demo.opensearch.UnsafeOpenSearchClientFactory
import com.example.demo.opensearch.startup.PrepareOpenSearchIndex
import com.example.demo.service.query.FullTextSimilaritySearchService
import com.example.demo.service.segmentation.Segmenter
import com.example.demo.service.store.FulltextSegmentedDocumentService
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import org.apache.hc.core5.http.HttpHost
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.transport.OpenSearchTransport
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FullTextConfig {

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

    @Bean
    fun opensearchUnsegmentedService(segmenter: Segmenter, client: OpenSearchClient) = UnsegmentedDocumentServiceImpl(
        segmenter = segmenter,
        segmentedDocumentService = FulltextSegmentedDocumentService(client)
    )

    @Bean
    fun fullTextSimilaritySearchService(client: OpenSearchClient) =
        FullTextSimilaritySearchService(client)
}