package com.example.demo.config.properties.options

import org.springframework.boot.context.properties.ConfigurationProperties

class FullTextSearchOptions {

    var enabled: Boolean = false
        @ConfigurationProperties("enabled")
        set
}