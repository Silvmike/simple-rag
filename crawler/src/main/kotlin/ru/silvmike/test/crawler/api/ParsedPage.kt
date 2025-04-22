package ru.silvmike.test.crawler.api

data class ParsedPage(
    val url: String,
    val urls: List<String>,
    val meaningfulContent: List<String>?
)
