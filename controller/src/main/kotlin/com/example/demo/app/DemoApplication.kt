package com.example.demo.app

import com.example.demo.config.*
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
