package com.example.demo.containers

import jakarta.annotation.PreDestroy
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class SeleniumChromeContainer :
    GenericContainer<SeleniumChromeContainer>(
        DockerImageName.parse("selenium/standalone-chrome:latest")
    ),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    init {
        shmSize = 2L * 1024L * 1024L * 1024L
        addFixedExposedPort(4444, 4444)
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {

        with(applicationContext.environment) {
            if (getProperty("options.yandex-search-client.enabled") == "false") {
                return
            } else if (getProperty("options.yandex-search-client.driver.local") == "true") {
                return
            }
        }

        start()

        TestPropertyValues.of(
            "options.yandex-search-client.driver.hubUrl=http://127.0.0.1:4444/wd/hub",
        ).applyTo(applicationContext.environment)
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}