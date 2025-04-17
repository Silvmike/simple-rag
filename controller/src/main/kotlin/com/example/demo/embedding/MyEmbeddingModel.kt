package com.example.demo.embedding

import com.example.demo.embedding.api.EmbedClient
import org.springframework.ai.document.Document
import org.springframework.ai.embedding.Embedding
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.embedding.EmbeddingResponse

class MyEmbeddingModel(
    private val client: EmbedClient
) : EmbeddingModel {

    override fun call(request: EmbeddingRequest) =
        EmbeddingResponse(
            client.embed(
                request.instructions
            ).mapIndexed {
                index, result -> Embedding(result.toFloatArray(), index)
            }
        )

    override fun embed(document: Document): FloatArray =
        client.embed(listOf(document.formattedContent))[0].toFloatArray()

    override fun dimensions(): Int = 1024
}