package com.example.demo.config.properties.options

import org.springframework.boot.context.properties.ConfigurationProperties

class VectorSearchOptions {

    var enabled: Boolean = false
        @ConfigurationProperties("enabled")
        set
}