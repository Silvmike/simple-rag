package com.example.demo.app

import com.example.demo.config.DbConfig
import com.example.demo.config.EmbeddingModelClientConfig
import com.example.demo.config.RestConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(value = [RestConfig::class, DbConfig::class, EmbeddingModelClientConfig::class])
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
