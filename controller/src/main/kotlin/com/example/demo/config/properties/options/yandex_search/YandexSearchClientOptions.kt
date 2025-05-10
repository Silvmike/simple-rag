package com.example.demo.config.properties.options.yandex_search

import org.springframework.boot.context.properties.NestedConfigurationProperty

class YandexSearchClientOptions {
    var enabled: Boolean = false

    @NestedConfigurationProperty
    var driver: WebDriverOptions = WebDriverOptions()
}