package com.example.demo.chat.oauth

import com.example.demo.chat.oauth.api.AuthKeyProvider
import com.example.demo.chat.oauth.api.OAuth2Client
import com.example.demo.chat.oauth.api.OAuthResponse
import com.example.demo.chat.oauth.api.TokenProvider
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class OAuthTokenProvider(
    private val client: OAuth2Client,
    private val authKeyProvider: AuthKeyProvider,
    private val thresholdMs: Long = TimeUnit.MINUTES.toMillis(1)
) : TokenProvider {

    private val lock = ReentrantLock()
    private val reference = AtomicReference<OAuthResponse>()

    override fun provide(): String {
        val existing = reference.get()
        if (existing != null && notExpired(existing)) {
            return existing.accessToken
        }
        lock.withLock {
            val possiblyValid = reference.get()
            if (possiblyValid != null && notExpired(possiblyValid)) {
                return possiblyValid.accessToken
            }
            val result = client.requestToken(authKeyProvider.provide())
            reference.set(result)
            return result.accessToken
        }
    }

    private fun notExpired(existing: OAuthResponse) =
        (existing.expiresAt - thresholdMs) > System.currentTimeMillis()
}