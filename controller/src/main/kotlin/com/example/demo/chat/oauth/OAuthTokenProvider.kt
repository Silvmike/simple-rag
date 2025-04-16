package com.example.demo.chat.oauth

import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class OAuthTokenProvider(
    private val client: OAuth2Client,
    private val authKey: String
) : TokenProvider {

    private val lock = ReentrantLock()
    private val reference = AtomicReference<OAuthResponse>()

    override fun provider(): String {
        val existing = reference.get()
        if (existing != null && existing.expiresAt < System.currentTimeMillis()) {
            return existing.accessToken
        }
        lock.withLock {
            val result = client.requestToken(authKey)
            reference.set(result)
            return result.accessToken
        }
    }
}