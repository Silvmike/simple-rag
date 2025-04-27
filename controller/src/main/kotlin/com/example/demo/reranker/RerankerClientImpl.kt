package com.example.demo.reranker

import com.example.demo.embedding.api.UrlProvider
import com.example.demo.reranker.api.RerankRequest
import com.example.demo.reranker.api.RerankResponse
import com.example.demo.reranker.api.RerankerClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

class RerankerClientImpl(
    private val httpClient: HttpClient,
    private val urlProvider: UrlProvider
) : RerankerClient {

    private val mapper = ObjectMapper()
        .registerModules(KotlinModule.Builder().build())

    override fun rerank(request: RerankRequest): RerankResponse =
        httpClient
            .send(
                HttpRequest.newBuilder()
                    .uri(URI(urlProvider.url()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(
                        BodyPublishers.ofString(
                            mapper.writeValueAsString(request)
                        )
                    )
                    .build(),
                BodyHandlers.ofInputStream()
            ).let {
                return mapper.readValue(it.body(), RerankResponse::class.java)
            }
}