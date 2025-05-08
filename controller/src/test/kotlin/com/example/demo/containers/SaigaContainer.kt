package com.example.demo.containers

import jakarta.annotation.PreDestroy
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import java.time.Duration

class SaigaContainer :
    GenericContainer<SaigaContainer>(
        DockerImageName.parse("test-saiga:latest")
    ),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    init {
        addFixedExposedPort(11434, 11434)
        waitingFor(
            Wait.forHttp("/").forPort(11434)
                .withStartupTimeout(Duration.ofSeconds(60))
        );
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {

        if (applicationContext.environment.getProperty("options.model.ollama.enabled") != "true") {
            return
        }
        if (applicationContext.environment.getProperty("options.model.ollama.name") != "ilyagusev/saiga_llama3") {
            return
        }

        start()
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}