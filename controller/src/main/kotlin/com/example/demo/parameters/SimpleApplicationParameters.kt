package com.example.demo.parameters

class SimpleApplicationParameters(
    override val fullTextSearchMaxResults: Int,
    override val vectorSearchMaxResults: Int,
    override val rerankerMaxResults: Int
) : ApplicationParameters