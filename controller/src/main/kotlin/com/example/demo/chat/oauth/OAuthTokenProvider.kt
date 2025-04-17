package com.example.demo.chat.oauth

import com.example.demo.chat.oauth.api.AuthKeyProvider
import com.example.demo.chat.oauth.api.OAuth2Client
import com.example.demo.chat.oauth.api.OAuthResponse
import com.example.demo.chat.oauth.api.TokenProvider
import com.example.demo.datetime.LocalDateTimeProvider
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Supplier
import kotlin.concurrent.withLock

class OAuthTokenProvider(
    private val client: OAuth2Client,
    private val authKeyProvider: AuthKeyProvider,
    private val localDateTimeProvider: LocalDateTimeProvider,
    private val thresholdMs: Long = TimeUnit.MINUTES.toMillis(1)
) : TokenProvider {

    private val lock = ReentrantLock()
    private val reference = AtomicReference<OAuthResponse>()

    override fun provide(): String =
        reference.validTokenOrElse {
            lock.withLock {
                reference.validTokenOrElse {
                    client.requestToken(authKeyProvider.provide()).apply {
                        reference.set(this)
                    }
                }
            }
        }.accessToken

    private fun AtomicReference<OAuthResponse>.validTokenOrElse(supplier: Supplier<OAuthResponse>) =
        Optional.ofNullable(get())
            .filter { notExpired(it) }
            .orElseGet(supplier)

    private fun notExpired(existing: OAuthResponse) =
        (existing.expiresAt - thresholdMs) > localDateTimeProvider.currentTimeMillis()
}