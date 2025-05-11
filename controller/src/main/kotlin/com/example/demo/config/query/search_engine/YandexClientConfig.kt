package com.example.demo.config.query.search_engine

import com.example.demo.config.properties.Options
import com.example.demo.search_engines.yandex.YandexSearchEngineClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = ["options.search-engine.yandex-search-client.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Configuration
class YandexClientConfig {

    @Bean
    fun yandexSearchClient(options: Options) = YandexSearchEngineClient(options)
}