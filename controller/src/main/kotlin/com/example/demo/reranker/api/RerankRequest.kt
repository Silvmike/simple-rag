package com.example.demo.reranker.api

import com.fasterxml.jackson.annotation.JsonProperty

data class RerankRequest(
    val query: String,
    @JsonProperty("docs")
    val documents: List<String>,
    @JsonProperty("top_k")
    val topK: Int
)
