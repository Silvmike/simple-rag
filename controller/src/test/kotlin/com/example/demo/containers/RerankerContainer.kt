package com.example.demo.containers

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.delay
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.concurrent.TimeUnit

class RerankerContainer :
    GenericContainer<RerankerContainer>(
        DockerImageName.parse("test-reranker:latest")
    ),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    init {
        addFixedExposedPort(8001, 8001)
        waitingFor(
            Wait.forHttp("/health").withStartupTimeout(Duration.ofSeconds(30))
        );
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        start()
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}