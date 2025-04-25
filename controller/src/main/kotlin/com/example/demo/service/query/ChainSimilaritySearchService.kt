package com.example.demo.service.query

import com.example.demo.service.query.api.SimilaritySearchService

class ChainSimilaritySearchService(
    private val chain: List<SimilaritySearchService>
) : SimilaritySearchService {
    override fun search(query: String): List<String> =
        chain.flatMap { it.search(query) }
            .distinct()

}