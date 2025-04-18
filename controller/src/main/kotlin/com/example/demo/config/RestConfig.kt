package com.example.demo.config

import com.example.demo.rest.DigestRest
import com.example.demo.service.store.UnsegmentedDocumentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestConfig {

    @Bean
    fun digestRest(unsegmentedDocumentService: UnsegmentedDocumentService) =
        DigestRest(unsegmentedDocumentService)

}