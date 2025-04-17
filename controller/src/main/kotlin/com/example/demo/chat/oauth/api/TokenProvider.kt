package com.example.demo.chat.oauth.api

interface TokenProvider {

    fun provide(): String

}