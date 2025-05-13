package com.example.demo.chat.api

interface MyChat {

    fun exchange(message: String, options: Map<String, String> = emptyMap()): String

}