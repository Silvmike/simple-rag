package com.example.demo.search_engines.api

interface SearchEngineClient {

    fun query(query: String): List<SearchResult>
}