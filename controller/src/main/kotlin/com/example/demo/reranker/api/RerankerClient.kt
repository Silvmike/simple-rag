package com.example.demo.reranker.api

interface RerankerClient {

    fun rerank(request: RerankRequest): RerankResponse
}