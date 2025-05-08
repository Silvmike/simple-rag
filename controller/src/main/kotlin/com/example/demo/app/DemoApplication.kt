package com.example.demo.app

import com.example.demo.config.*
import com.example.demo.config.misc.DisableJpaConfiguration
import com.example.demo.config.misc.DisableQdrantConfiguration
import com.example.demo.config.model.DeepSeekClientConfig
import com.example.demo.config.model.GigaChatClientConfig
import com.example.demo.config.model.OllamaClientConfig
import com.example.demo.config.query.QueryServiceConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [
	GigaChatClientConfig::class,
	OllamaClientConfig::class,
	DeepSeekClientConfig::class,
	CommonConfig::class,
	QueryServiceConfig::class,
	CommonRestConfig::class,
	DisableJpaConfiguration::class,
	DisableQdrantConfiguration::class
])
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
