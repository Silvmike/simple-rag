package com.example.demo.config

import com.example.demo.rest.DigestRest
import com.example.demo.rest.QueryRest
import com.example.demo.rest.StoreRest
import com.example.demo.service.query.QueryService
import com.example.demo.service.store.UnsegmentedDocumentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestConfig {

    @Bean
    fun digestRest(unsegmentedDocumentService: UnsegmentedDocumentService) =
        DigestRest(unsegmentedDocumentService)

    @Bean
    fun queryRest(queryService: QueryService) = QueryRest(queryService)

    @Bean
    fun storeRest(unsegmentedDocumentService: UnsegmentedDocumentService) = StoreRest(unsegmentedDocumentService)

}