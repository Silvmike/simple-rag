package com.example.demo.containers

import jakarta.annotation.PreDestroy
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class DatabaseContainer :
    PostgreSQLContainer<DatabaseContainer>(DockerImageName.parse("postgres:16.8")),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        start()
        TestPropertyValues.of(
            "spring.datasource.url=$jdbcUrl",
            "spring.datasource.username=$username",
            "spring.datasource.password=$password"
        ).applyTo(applicationContext.environment);
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}