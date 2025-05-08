package com.example.demo.config.properties.options.model

import org.springframework.boot.context.properties.ConfigurationProperties

class ModelOptions {

    var enabled: Boolean = false
        @ConfigurationProperties("enabled")
        set

}