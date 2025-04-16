package com.example.demo.chat

interface MyChatClient {

    fun exchange(query: String): String
}