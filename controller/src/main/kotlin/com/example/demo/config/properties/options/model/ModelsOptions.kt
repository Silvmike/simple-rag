package com.example.demo.config.properties.options.model

import org.springframework.boot.context.properties.ConfigurationProperties

class ModelsOptions {

    var deepseek: ModelOptions = ModelOptions()
        @ConfigurationProperties("deep-seek")
        set

    var ollama: OllamaModelOptions = OllamaModelOptions()
        @ConfigurationProperties("ollama")
        set

    var gigaChat: ModelOptions = ModelOptions()
        @ConfigurationProperties("giga-chat")
        set

}