package com.example.demo.chat.oauth

import com.example.demo.chat.oauth.api.AuthKeyProvider

private const val AUTH_KEY_ENV_NAME = "OPEN_API_AUTH_KEY"

class EnvironmentAuthKeyProvider : AuthKeyProvider {

    override fun provide(): String =
        System.getenv().getOrElse(AUTH_KEY_ENV_NAME) {
            throw IllegalStateException("Env key $AUTH_KEY_ENV_NAME is not set!")
        }
}