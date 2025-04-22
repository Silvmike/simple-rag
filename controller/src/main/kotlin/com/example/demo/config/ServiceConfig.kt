package com.example.demo.config

import com.example.demo.chat.api.MyChat
import com.example.demo.datetime.DefaultLocalDateTimeProvider
import com.example.demo.service.query.ChainSimilaritySearchService
import com.example.demo.service.query.QueryService
import com.example.demo.service.query.VectorStoreSimilaritySearchService
import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.service.segmentation.BaseSegmenter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(value = [
    DbConfig::class,
    VectorConfig::class,
    FullTextConfig::class
])
@Configuration
class ServiceConfig {

    @Bean
    fun localDateTimeProvider() = DefaultLocalDateTimeProvider

    @Bean
    fun queryService(
        chain: List<SimilaritySearchService>,
        chat: MyChat
    ) = QueryService(ChainSimilaritySearchService(chain), chat)

    @Bean
    fun vectorSimilaritySearchService(vectorStore: VectorStore) =
        VectorStoreSimilaritySearchService(vectorStore)

    @Bean
    fun segmenter() = BaseSegmenter()
}