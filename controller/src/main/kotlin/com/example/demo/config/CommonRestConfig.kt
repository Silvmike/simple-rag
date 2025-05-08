package com.example.demo.config

import com.example.demo.chat.api.MyChat
import com.example.demo.rest.ChatRest
import com.example.demo.rest.DigestRest
import com.example.demo.rest.StoreRest
import com.example.demo.service.store.ComplexUnsegmentedDocumentService
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonRestConfig {

    @Bean
    fun digestRest(services: List<UnsegmentedDocumentServiceImpl>) =
        DigestRest(ComplexUnsegmentedDocumentService(services))

    @Bean
    fun storeRest(services: List<UnsegmentedDocumentServiceImpl>) =
        StoreRest(ComplexUnsegmentedDocumentService(services))

    @Bean
    @ConditionalOnProperty(
        name = ["options.test-chat.enabled"],
        havingValue = "true",
        matchIfMissing = false
    )
    fun chatRest(myChat: MyChat) = ChatRest(myChat)

}