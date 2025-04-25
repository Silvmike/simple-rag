package com.example.demo.chat.deepseek.api

interface DeepSeekClient {

    fun exchange(request: DeepSeekRequest): DeepSeekResponse

}