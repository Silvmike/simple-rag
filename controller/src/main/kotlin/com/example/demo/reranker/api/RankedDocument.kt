package com.example.demo.reranker.api

data class RankedDocument(
    val document: String,
    val score: Float
)