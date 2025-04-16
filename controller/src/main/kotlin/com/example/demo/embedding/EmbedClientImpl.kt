package com.example.demo.embedding

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

class EmbedClientImpl(
    private val httpClient: HttpClient,
    private val urlProvider: UrlProvider
): EmbedClient {

    private val mapper = ObjectMapper()
        .registerModules(KotlinModule.Builder().build())

    private val floatList = TypeFactory.defaultInstance()
        .constructType(object : TypeReference<MutableList<MutableList<Float>>>() {})

    override fun embed(values: List<String>): List<List<Float>> {
        val response = httpClient
            .send(
                HttpRequest.newBuilder()
                    .uri(URI(urlProvider.url()))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(
                        BodyPublishers.ofString(
                        mapper.writeValueAsString(values)
                    ))
                    .build(),
                BodyHandlers.ofInputStream()
            )

        val list = mapper.readValue<MutableList<MutableList<Float>>>(
            response.body(),
            floatList
        )
        return list
    }
}