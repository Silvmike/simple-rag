package com.example.demo.service.query.api

interface SimilaritySearchService {

    fun search(query: String): List<String>
}