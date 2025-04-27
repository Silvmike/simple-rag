package com.example.demo.service.query

import com.example.demo.parameters.ApplicationParameters
import com.example.demo.service.query.api.SimilaritySearchService
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered

class VectorStoreSimilaritySearchService(
    private val vectorStore: VectorStore,
    private val applicationParameters: ApplicationParameters
): SimilaritySearchService, Ordered {

    override fun search(query: String): List<String> =
        vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query.lowercase())
                .topK(applicationParameters.vectorSearchMaxResults)
                .build()
        )!!.map { it.text!! }

    override fun getOrder(): Int = 2

}