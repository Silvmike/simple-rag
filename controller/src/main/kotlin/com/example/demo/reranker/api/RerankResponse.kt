package com.example.demo.reranker.api

import com.fasterxml.jackson.annotation.JsonProperty

data class RerankResponse(
    @JsonProperty("reranked_docs")
    val documents: List<RankedDocument>
)
