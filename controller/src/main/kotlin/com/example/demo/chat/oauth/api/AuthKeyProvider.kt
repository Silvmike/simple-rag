package com.example.demo.chat.oauth.api

interface AuthKeyProvider {

    fun provide(): String
}