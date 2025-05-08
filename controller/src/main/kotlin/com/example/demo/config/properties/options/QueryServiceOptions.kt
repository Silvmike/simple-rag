package com.example.demo.config.properties.options

import org.springframework.boot.context.properties.ConfigurationProperties

class QueryServiceOptions {

    var enabled: Boolean = false
        @ConfigurationProperties("enabled")
        set
}