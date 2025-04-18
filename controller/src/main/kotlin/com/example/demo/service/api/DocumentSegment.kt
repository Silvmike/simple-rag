package com.example.demo.service.api

data class DocumentSegment(
    val fragment: String,
    val startPos: Int,
    val endPos: Int
)