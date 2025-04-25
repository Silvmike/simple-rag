package com.example.demo.config

import com.example.demo.chat.api.MyChat
import com.example.demo.util.datetime.DefaultLocalDateTimeProvider
import com.example.demo.service.query.ChainSimilaritySearchService
import com.example.demo.service.query.QueryService
import com.example.demo.service.query.advice.MyChatPromptAdviceQueryEnricher
import com.example.demo.service.query.advice.QueryAdvisingSimilaritySearchService
import com.example.demo.service.query.advice.QueryEnricher
import com.example.demo.service.query.api.SimilaritySearchService
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
        chat: MyChat,
        queryEnricher: QueryEnricher
    ) = QueryService(
        similaritySearchService = QueryAdvisingSimilaritySearchService(
            delegate = ChainSimilaritySearchService(chain),
            queryEnricher = queryEnricher
        ),
        chat = chat
    )

    @Bean
    fun queryEnricher(chat: MyChat): QueryEnricher =
        MyChatPromptAdviceQueryEnricher(chat)

}