package com.example.demo.parameters

interface ApplicationParameters {

    val fullTextSearchMaxResults: Int
    val vectorSearchMaxResults: Int
    val rerankerMaxResults: Int

}