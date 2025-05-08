package com.example.demo.config

import com.example.demo.rest.FullTextAdviceRest
import com.example.demo.service.query.advice.QueryEnricher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FullTextRestConfig {

    @Bean
    fun fullTextAdviceRest(queryEnricher: QueryEnricher) =
        FullTextAdviceRest(queryEnricher)
}