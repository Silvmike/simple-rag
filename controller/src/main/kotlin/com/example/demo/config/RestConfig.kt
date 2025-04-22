package com.example.demo.config

import com.example.demo.rest.DigestRest
import com.example.demo.rest.QueryRest
import com.example.demo.rest.StoreRest
import com.example.demo.service.query.QueryService
import com.example.demo.service.store.ComplexUnsegmentedDocumentService
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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

}