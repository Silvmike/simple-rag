package com.example.demo.chat.planning.model

data class Communication(
    val query: String,
    val plan: List<FunctionCall>,
    val finalResponse: String? = null,
    val resolved: Boolean = false
)