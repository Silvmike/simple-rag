package com.example.demo.chat.oauth

import com.example.demo.chat.oauth.api.AuthKeyProvider
import com.example.demo.chat.oauth.api.OAuth2Client
import com.example.demo.chat.oauth.api.OAuthResponse
import com.example.demo.chat.oauth.api.TokenProvider
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class OAuthTokenProvider(
    private val client: OAuth2Client,
    private val authKeyProvider: AuthKeyProvider
) : TokenProvider {

    private val lock = ReentrantLock()
    private val reference = AtomicReference<OAuthResponse>()

    override fun provide(): String {
        val existing = reference.get()
        if (existing != null && existing.expiresAt < System.currentTimeMillis()) {
            return existing.accessToken
        }
        lock.withLock {
            val result = client.requestToken(authKeyProvider.provide())
            reference.set(result)
            return result.accessToken
        }
    }
}