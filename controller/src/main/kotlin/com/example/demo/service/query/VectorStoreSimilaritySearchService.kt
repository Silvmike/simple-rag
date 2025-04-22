package com.example.demo.service.query

import com.example.demo.service.query.api.SimilaritySearchService
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore

class VectorStoreSimilaritySearchService(
    private val vectorStore: VectorStore,
    private val topK: Int = 5
): SimilaritySearchService {

    override fun search(query: String): List<String> =
        vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query.lowercase())
                .topK(topK)
                .build()
        )!!.map { it.text!! }

}