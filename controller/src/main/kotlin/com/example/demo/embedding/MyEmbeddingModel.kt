package com.example.demo.embedding

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.embedding.EmbeddingResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

//TODO
class MyEmbeddingModel(
    private val urlProvider: UrlProvider,
    private val httpClient: HttpClient
) : EmbeddingModel {

    private val mapper = ObjectMapper()
        .registerModules(KotlinModule.Builder().build())

    private val floatList = TypeFactory.defaultInstance()
        .constructCollectionType(MutableList::class.java, Float::class.java)

    override fun call(request: EmbeddingRequest) =
        URI.create(urlProvider.url()).let { uri ->
            EmbeddingResponse(
                request.instructions.mapIndexed { index, value ->
                    val list = embed(uri, value)
                    Embedding(list.toFloatArray(), index)
                }
            )
        }

    private fun embed(uri: URI, value: String): MutableList<Float> {
        val response = httpClient
            .send(
                HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(BodyPublishers.ofString(value))
                    .build(),
                BodyHandlers.ofInputStream()
            )

        val list = mapper.readValue<MutableList<Float>>(
            response.body(),
            floatList
        )
        return list
    }

    override fun embed(document: Document): FloatArray =
        URI.create(urlProvider.url()).let { uri ->
            embed(uri, document.formattedContent).toFloatArray()
        }
}