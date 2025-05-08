package com.example.demo.config

import com.example.demo.chat.EnvironmentTokenProvider
import com.example.demo.chat.GigaChatMyChatImpl
import com.example.demo.chat.ModelSelectorImpl
import com.example.demo.chat.UnsafeClientFactory
import com.example.demo.chat.api.ModelSelector
import com.example.demo.chat.api.TokenProvider
import com.example.demo.chat.giga.GigaChatClientImpl
import com.example.demo.chat.giga.api.GigaChatClient
import com.example.demo.chat.oauth.OAuth2ClientImpl
import com.example.demo.chat.oauth.OAuthTokenProvider
import com.example.demo.util.datetime.LocalDateTimeProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConditionalOnProperty(
    name = ["options.model.giga-chat.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
@Configuration
class GigaChatClientConfig {

    @Bean
    fun oAuthTokenProvider(localDateTimeProvider: LocalDateTimeProvider) =
        OAuthTokenProvider(
            OAuth2ClientImpl(UnsafeClientFactory.create()),
            EnvironmentTokenProvider("GIGA_CHAT_API_KEY"),
            localDateTimeProvider
        )

    @Bean
    fun gigaChatClient(tokenProvider: TokenProvider) = GigaChatClientImpl(tokenProvider, UnsafeClientFactory.create())

    @Bean
    fun modelSelector(gigaChatClient: GigaChatClient) =
        ModelSelectorImpl(gigaChatClient)

    @Bean
    fun myChat(
        gigaChatClient: GigaChatClient,
        modelSelector: ModelSelector
    ) = GigaChatMyChatImpl(gigaChatClient, modelSelector)

}