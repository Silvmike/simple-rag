package com.example.demo.config

import com.example.demo.rest.TestController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestConfig {

    @Bean
    fun testController() = TestController()

}