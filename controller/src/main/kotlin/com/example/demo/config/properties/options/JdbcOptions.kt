package com.example.demo.config.properties.options

import org.springframework.boot.context.properties.ConfigurationProperties

class JdbcOptions {

    var enabled: Boolean = false
        @ConfigurationProperties("enabled")
        set
}