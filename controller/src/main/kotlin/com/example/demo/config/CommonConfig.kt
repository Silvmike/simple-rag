package com.example.demo.config

import com.example.demo.config.properties.Options
import com.example.demo.util.datetime.DefaultLocalDateTimeProvider
import com.example.demo.util.file.DirectoryObserved
import com.example.demo.util.file.SelfStartedFileAlterationMonitor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(Options::class)
@Configuration
class CommonConfig {

    @Bean
    fun localDateTimeProvider() = DefaultLocalDateTimeProvider

    @Bean
    fun selfStartedFileAlterationMonitor(observed: List<DirectoryObserved>) =
        SelfStartedFileAlterationMonitor(observed)
}