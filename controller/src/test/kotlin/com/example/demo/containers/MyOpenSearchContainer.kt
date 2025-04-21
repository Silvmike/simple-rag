package com.example.demo.containers

import jakarta.annotation.PreDestroy
import org.opensearch.testcontainers.OpensearchContainer
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.utility.DockerImageName

class MyOpenSearchContainer :
    OpensearchContainer<MyOpenSearchContainer>(DockerImageName.parse("opensearchproject/opensearch:2.0.0")),
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        addFixedExposedPort(9200, 9200)
        start()
    }

    @PreDestroy
    override fun stop() {
        super.stop()
    }

}