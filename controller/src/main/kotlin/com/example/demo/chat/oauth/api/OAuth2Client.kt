package com.example.demo.chat.oauth.api

import java.util.*

interface OAuth2Client {

    fun requestToken(clientId: String, clientSecret: String): OAuthResponse =
        requestToken(
            Base64.getEncoder()
                .encodeToString("$clientId:$clientSecret".toByteArray())
        )

    fun requestToken(authKey: String): OAuthResponse
}