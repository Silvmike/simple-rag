package com.example.demo.embedding.api

interface EmbedClient {

    fun embed(data: List<String>): List<List<Float>>

}