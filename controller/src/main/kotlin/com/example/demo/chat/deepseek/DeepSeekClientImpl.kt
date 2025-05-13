package com.example.demo.chat.deepseek

import com.example.demo.chat.deepseek.api.DeepSeekClient
import com.example.demo.chat.deepseek.api.DeepSeekRequest
import com.example.demo.chat.deepseek.api.DeepSeekResponse
import com.example.demo.chat.api.TokenProvider
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.Charset

class DeepSeekClientImpl(
    private val httpClient: OkHttpClient,
    private val baseUrl: String = "https://api.deepseek.com",
    private val tokenProvider: TokenProvider,
) : DeepSeekClient {

    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .registerModules(
            KotlinModule.Builder().build(),
            JavaTimeModule()
        )

    override fun exchange(request: DeepSeekRequest): DeepSeekResponse {
        httpClient.newCall(
            Request.Builder()
                .url("$baseUrl/chat/completions")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer ${tokenProvider.provide()}")
                .post(
                    mapper.writeValueAsString(
                        request
                    ).toRequestBody(
                        "application/json".toMediaTypeOrNull()
                    )
                )
                .build()
        ).execute().let {
            if (it.code != 200) {
                throw IllegalStateException("DeepSeek completion returned status ${it.code} ${it.message}")
            }
            return mapper.readValue(
                it.body!!.source().readString(Charset.defaultCharset()),
                DeepSeekResponse::class.java
            )
        }
    }

}