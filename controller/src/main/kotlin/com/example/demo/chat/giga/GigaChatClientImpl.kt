package com.example.demo.chat.giga

import com.example.demo.chat.giga.api.GigaChatClient
import com.example.demo.chat.giga.model.Chat
import com.example.demo.chat.giga.model.ChatCompletion
import com.example.demo.chat.giga.model.Models
import com.example.demo.chat.api.TokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.Charset
import java.util.*


class GigaChatClientImpl(
    private val tokenProvider: TokenProvider,
    private val httpClient: OkHttpClient
) : GigaChatClient {

    private val mapper = ObjectMapper()
        .registerModules(KotlinModule.Builder().build())

    override fun getCompletions(chat: Chat): ChatCompletion =
        makeCompletionsCall(tokenProvider.provide(), chat).let { responseBody ->
            mapper.readValue<ChatCompletion>(responseBody)
        }

    private fun makeCompletionsCall(token: String, chat: Chat): String =
        httpClient.newCall(
            Request.Builder()
                .url("https://gigachat.devices.sberbank.ru/api/v1/chat/completions")
                .header("Accept", "application/json")
                .header("RqUID", UUID.randomUUID().toString())
                .header("Authorization", "Bearer $token")
                .post(
                    mapper.writeValueAsString(chat).toRequestBody(
                        "application/json".toMediaTypeOrNull()
                    )
                )
                .build()
        ).execute().let {
            if (it.code != 200) {
                throw IllegalStateException("Giga chat completion returned status ${it.code} ${it.message}")
            }
            it.body!!.source().readString(Charset.defaultCharset())
        }

    override fun getModels(): Models =
        makeModelsCall(tokenProvider.provide()).let { responseBody ->
            mapper.readValue<Models>(responseBody)
        }

    private fun makeModelsCall(token: String): String =
        httpClient.newCall(
            Request.Builder()
                .url("https://gigachat.devices.sberbank.ru/api/v1/models")
                .header("Accept", "application/json")
                .header("RqUID", UUID.randomUUID().toString())
                .header("Authorization", "Bearer $token")
                .get()
                .build()
        ).execute().let {
            if (it.code != 200) {
                throw IllegalStateException("Giga chat completion returned status ${it.code} ${it.message}")
            }
            it.body!!.source().readString(Charset.defaultCharset())
        }

}