package ru.silvmike.test.crawler.api

fun interface ResultListener {

    fun onPageParsed(page: ParsedPage)
}