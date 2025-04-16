package com.example.demo.embedding

interface EmbedClient {

    fun embed(data: List<String>): List<List<Float>>

}