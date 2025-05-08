package com.example.demo.config

import com.example.demo.rest.QueryRest
import com.example.demo.service.query.QueryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryRestConfig {

    @Bean
    fun queryRest(queryService: QueryService) = QueryRest(queryService)

}