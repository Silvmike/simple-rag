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

class EmbedContainer :
    GenericContainer<EmbedContainer>(
        DockerImageName.parse("test-embed:latest")
    ),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    init {
        addFixedExposedPort(8000, 8000)
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