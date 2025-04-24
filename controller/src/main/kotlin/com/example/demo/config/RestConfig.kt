package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.chat.api.MyChat
import com.example.demo.rest.*
import com.example.demo.service.query.QueryService
import com.example.demo.service.query.advice.QueryEnricher
import com.example.demo.service.store.ComplexUnsegmentedDocumentService
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class RestConfig {

    @Bean
    fun digestRest(services: List<UnsegmentedDocumentServiceImpl>) =
        DigestRest(ComplexUnsegmentedDocumentService(services))

    @Bean
    fun queryRest(queryService: QueryService) = QueryRest(queryService)

    @Bean
    fun storeRest(services: List<UnsegmentedDocumentServiceImpl>) =
        StoreRest(ComplexUnsegmentedDocumentService(services))

    @Bean
    @Profile(Profiles.FULL_TEXT)
    fun fullTextAdviceRest(queryEnricher: QueryEnricher) =
        FullTextAdviceRest(queryEnricher)

    @Bean
    @Profile(Profiles.TEST_CHAT)
    fun chatRest(myChat: MyChat) = ChatRest(myChat)

}