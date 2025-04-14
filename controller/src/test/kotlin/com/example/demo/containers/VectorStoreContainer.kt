package com.example.demo.containers

import jakarta.annotation.PreDestroy
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import kotlin.random.Random

class VectorStoreContainer :
    GenericContainer<VectorStoreContainer>(DockerImageName.parse("qdrant/qdrant:v1.13.6")),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    private val port = Random.nextInt(3000, 6000)

    init {
        addFixedExposedPort(port, 6334)
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        start()
        TestPropertyValues.of(
            "spring.ai.qdrant.host=localhost",
            "spring.ai.qdrant.port=$port",
        ).applyTo(applicationContext.environment);
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}