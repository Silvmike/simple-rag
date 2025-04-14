package com.example.demo.embedding

import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.embedding.EmbeddingRequest
import org.springframework.ai.embedding.EmbeddingResponse

class StubEmbeddingModel : EmbeddingModel {

    override fun call(request: EmbeddingRequest): EmbeddingResponse {
        TODO("Not yet implemented")
    }

    override fun embed(document: Document): FloatArray {
        TODO("Not yet implemented")
    }
}