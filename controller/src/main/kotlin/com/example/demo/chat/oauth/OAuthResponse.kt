package com.example.demo.chat.oauth

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuthResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("expires_at")
    val expiresAt: Long
)