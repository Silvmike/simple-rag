package com.example.demo.chat.ollama_deepseek

import com.example.demo.chat.ollama_deepseek.api.OllamaClient
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.nio.charset.Charset

class OllamaClientImpl(
    private val httpClient: OkHttpClient,
    private val baseUrl: String
) : OllamaClient {

    private val mapper = ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModules(
            KotlinModule.Builder().build(),
            JavaTimeModule()
        )

    override fun generate(prompt: String, model: String): OllamaResponse =
        httpClient.newCall(
            Request.Builder()
                .url("$baseUrl/api/generate")
                .header("Accept", "application/json")
                .post(
                    mapper.writeValueAsString(
                        OllamaRequest(model = model, prompt = prompt)
                    ).toRequestBody(
                        "application/json".toMediaTypeOrNull()
                    )
                )
                .build()
        ).execute().let {
            if (it.code != 200) {
                throw IllegalStateException("Giga chat completion returned status ${it.code} ${it.message}")
            }
            mapper.readValue(
                it.body!!.source().readString(Charset.defaultCharset()),
                OllamaResponse::class.java
            )
        }

}