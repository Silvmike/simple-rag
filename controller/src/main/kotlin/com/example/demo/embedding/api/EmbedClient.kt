package com.example.demo.embedding.api

interface EmbedClient {

    fun embed(documents: List<String>): List<List<Float>>

    fun dimensions(): Int
}