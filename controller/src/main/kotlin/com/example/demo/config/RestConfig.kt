package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.rest.DigestRest
import com.example.demo.rest.FullTextAdviceRest
import com.example.demo.rest.QueryRest
import com.example.demo.rest.StoreRest
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

}