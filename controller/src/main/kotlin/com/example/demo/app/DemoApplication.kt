package com.example.demo.app

import com.example.demo.config.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [
	DbConfig::class,
	EmbeddingModelClientConfig::class,
	GigaChatClientConfig::class,
	OllamaDeepSeekClientConfig::class,
	DeepSeekClientConfig::class,
	RerankerClientConfig::class,
	ServiceConfig::class,
	FullTextConfig::class,
	RestConfig::class
])
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
