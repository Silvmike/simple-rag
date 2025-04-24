package com.example.demo.service.query.advice

import com.example.demo.service.query.api.SimilaritySearchService

class QueryAdvisingSimilaritySearchService(
    private val delegate: SimilaritySearchService,
    private val queryEnricher: QueryEnricher
) : SimilaritySearchService {

    override fun search(query: String): List<String> =
        delegate.search(queryEnricher.enrich(query))

}