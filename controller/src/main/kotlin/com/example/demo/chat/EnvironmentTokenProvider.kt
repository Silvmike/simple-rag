package com.example.demo.chat

import com.example.demo.chat.api.TokenProvider

class EnvironmentTokenProvider(
    private val varName: String
) : TokenProvider {

    override fun provide(): String =
        System.getenv().getOrElse(varName) {
            throw IllegalStateException("Check your environment! $varName is not set!")
        }
}