package com.example.demo.config

import com.example.demo.chat.ModelSelectorImpl
import com.example.demo.chat.MyChatImpl
import com.example.demo.chat.UnsafeClientFactory
import com.example.demo.chat.api.ModelSelector
import com.example.demo.chat.giga.GigaChatClientImpl
import com.example.demo.chat.giga.api.GigaChatClient
import com.example.demo.chat.oauth.EnvironmentAuthKeyProvider
import com.example.demo.chat.oauth.OAuth2ClientImpl
import com.example.demo.chat.oauth.OAuthTokenProvider
import com.example.demo.chat.oauth.api.TokenProvider
import com.example.demo.datetime.DefaultLocalDateTimeProvider
import com.example.demo.datetime.LocalDateTimeProvider
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig {

    @Bean
    fun unsafeHttpClient() = UnsafeClientFactory.create()

    @Bean
    fun localDateTimeProvider() = DefaultLocalDateTimeProvider

    @Bean
    fun oAuthTokenProvider(httpClient: OkHttpClient,
                           localDateTimeProvider: LocalDateTimeProvider) =
        OAuthTokenProvider(
            OAuth2ClientImpl(httpClient),
            EnvironmentAuthKeyProvider(),
            localDateTimeProvider
        )

    @Bean
    fun gigaChatClient(
        tokenProvider: TokenProvider,
        httpClient: OkHttpClient
    ) = GigaChatClientImpl(tokenProvider, httpClient)

    @Bean
    fun modelSelector(gigaChatClient: GigaChatClient) =
        ModelSelectorImpl(gigaChatClient)

    @Bean
    fun myChat(
        gigaChatClient: GigaChatClient,
        modelSelector: ModelSelector
    ) = MyChatImpl(gigaChatClient, modelSelector)

}