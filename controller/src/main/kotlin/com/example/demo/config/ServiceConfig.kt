package com.example.demo.config

import com.example.demo.chat.api.MyChat
import com.example.demo.service.query.ChainSimilaritySearchService
import com.example.demo.service.query.QueryService
import com.example.demo.service.query.advice.MyChatPromptAdviceQueryEnricher
import com.example.demo.service.query.advice.QueryAdvisingSimilaritySearchService
import com.example.demo.service.query.advice.QueryEnricher
import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.util.datetime.DefaultLocalDateTimeProvider
import com.example.demo.util.file.DirectoryObserved
import com.example.demo.util.file.SelfStartedFileAlterationMonitor
import com.example.demo.util.template.FallbackTemplateProvider
import com.example.demo.util.template.FileSystemWatchedTemplateProvider
import com.example.demo.util.template.ResourceTemplateProvider
import com.example.demo.util.template.TemplateProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource

private const val ADVICE_QUERY_PROMPT_BEAN_NAME = "adviceQueryPromptTemplateProvider"
private const val QUERY_PROMPT_BEAN_NAME = "queryPromptTemplateProvider"
private const val FULL_TEXT_ADVICE_CLASSPATH = "/prompt/template/fulltext_advice.txt"
private const val QUERY_PROMPT_CLASSPATH = "/prompt/template/query.txt"

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
        queryEnricher: QueryEnricher,
        @Qualifier(QUERY_PROMPT_BEAN_NAME) provider: TemplateProvider
    ) = QueryService(
        similaritySearchService = QueryAdvisingSimilaritySearchService(
            delegate = ChainSimilaritySearchService(chain),
            queryEnricher = queryEnricher
        ),
        chat = chat,
        queryPromptTemplateProvider =
            FallbackTemplateProvider(
                main = provider,
                fallback = ResourceTemplateProvider(QUERY_PROMPT_CLASSPATH)
            )
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

    @Bean(ADVICE_QUERY_PROMPT_BEAN_NAME)
    fun watchedQueryEnricherPromptProvider() = FileSystemWatchedTemplateProvider(
        ClassPathResource(FULL_TEXT_ADVICE_CLASSPATH)
    )

    @Bean
    fun selfStartedFileAlterationMonitor(observed: List<DirectoryObserved>) =
        SelfStartedFileAlterationMonitor(observed)

}