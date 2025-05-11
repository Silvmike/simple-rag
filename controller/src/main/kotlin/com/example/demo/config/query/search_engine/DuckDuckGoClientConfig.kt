package com.example.demo.config.query.search_engine

import com.example.demo.chat.UnsafeClientFactory
import com.example.demo.search_engines.api.SearchEngineClient
import com.example.demo.search_engines.duckduckgo.DuckDuckGoSearchEngineClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = ["options.search-engine.duck-duck-go.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
@Configuration
class DuckDuckGoClientConfig {

    @Bean
    fun duckDuckGoConfig() = DuckDuckGoSearchEngineClient(UnsafeClientFactory.create())
}