package com.example.demo.config.query.yandex

import com.example.demo.config.properties.Options
import com.example.demo.search_engines.yandex.YandexSearchClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = ["options.yandex-search-client.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Configuration
class YandexClientConfig {

    @Bean
    fun yandexSearchClient(options: Options) = YandexSearchClient(options)
}