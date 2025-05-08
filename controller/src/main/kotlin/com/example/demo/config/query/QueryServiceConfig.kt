package com.example.demo.config.query

import com.example.demo.chat.api.MyChat
import com.example.demo.config.query.fulltext.FullTextConfig
import com.example.demo.config.query.vector.VectorConfig
import com.example.demo.parameters.ApplicationParameters
import com.example.demo.parameters.FileSystemWatchedApplicationParameters
import com.example.demo.parameters.SimpleApplicationParameters
import com.example.demo.reranker.api.RerankerClient
import com.example.demo.service.query.ChainSimilaritySearchService
import com.example.demo.service.query.QueryService
import com.example.demo.service.query.advice.MyChatPromptAdviceQueryEnricher
import com.example.demo.service.query.advice.QueryAdvisingSimilaritySearchService
import com.example.demo.service.query.advice.QueryEnricher
import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.util.template.FallbackTemplateProvider
import com.example.demo.util.template.FileSystemWatchedTemplateProvider
import com.example.demo.util.template.ResourceTemplateProvider
import com.example.demo.util.template.TemplateProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource

private const val ADVICE_QUERY_PROMPT_BEAN_NAME = "adviceQueryPromptTemplateProvider"
private const val QUERY_PROMPT_BEAN_NAME = "queryPromptTemplateProvider"
private const val FULL_TEXT_ADVICE_CLASSPATH = "/prompt/template/fulltext_advice.txt"
private const val QUERY_PROMPT_CLASSPATH = "/prompt/template/query.txt"

private const val APPLICATION_PARAMETERS_CLASSPATH = "/application_parameters.properties"

@ConditionalOnProperty(
    name = ["options.query.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Import(value = [
    QueryRestConfig::class,
    RerankerClientConfig::class,
    VectorConfig::class,
    FullTextConfig::class
])
@Configuration
class QueryServiceConfig {

    @Bean
    fun queryService(
        chain: List<SimilaritySearchService>,
        chat: MyChat,
        queryEnricher: QueryEnricher,
        rerankerClient: RerankerClient,
        @Qualifier(QUERY_PROMPT_BEAN_NAME) provider: TemplateProvider,
        params: ApplicationParameters
    ) = QueryService(
        similaritySearchService = QueryAdvisingSimilaritySearchService(
            delegate = ChainSimilaritySearchService(chain),
            queryEnricher = queryEnricher
        ),
        reranker = rerankerClient,
        chat = chat,
        queryPromptTemplateProvider =
            FallbackTemplateProvider(
                main = provider,
                fallback = ResourceTemplateProvider(QUERY_PROMPT_CLASSPATH)
            ),
        applicationParameters = params
    )

    @Bean(QUERY_PROMPT_BEAN_NAME)
    fun watchedQueryPromptProvider() = FileSystemWatchedTemplateProvider(
        ClassPathResource(QUERY_PROMPT_CLASSPATH)
    )

    @Bean
    fun queryEnricher(chat: MyChat, @Qualifier(ADVICE_QUERY_PROMPT_BEAN_NAME) provider: TemplateProvider): QueryEnricher =
        MyChatPromptAdviceQueryEnricher(
            chat = chat,
            promptTemplateProvider =
                FallbackTemplateProvider(
                    main = provider,
                    fallback = ResourceTemplateProvider(FULL_TEXT_ADVICE_CLASSPATH)
                )
        )

    @Bean
    fun watchedApplicationParameters() = FileSystemWatchedApplicationParameters(
        ClassPathResource(APPLICATION_PARAMETERS_CLASSPATH),
        SimpleApplicationParameters(
            fullTextSearchMaxResults = 10,
            vectorSearchMaxResults = 30,
            rerankerMaxResults = 3
        )
    )

    @Bean(ADVICE_QUERY_PROMPT_BEAN_NAME)
    fun watchedQueryEnricherPromptProvider() = FileSystemWatchedTemplateProvider(
        ClassPathResource(FULL_TEXT_ADVICE_CLASSPATH)
    )

}