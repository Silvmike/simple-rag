package com.example.demo.config.properties.options.search_engines

import com.example.demo.config.properties.options.search_engines.duckduckgo.DuckDuckGoClientOptions
import com.example.demo.config.properties.options.search_engines.yandex_search.YandexSearchClientOptions
import org.springframework.boot.context.properties.NestedConfigurationProperty

class SearchEnginesOptions {

    @NestedConfigurationProperty
    var yandexSearchClient: YandexSearchClientOptions = YandexSearchClientOptions()

    @NestedConfigurationProperty
    var duckDuckGo: DuckDuckGoClientOptions = DuckDuckGoClientOptions()
}