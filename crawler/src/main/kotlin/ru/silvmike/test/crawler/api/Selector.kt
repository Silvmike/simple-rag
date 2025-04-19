package ru.silvmike.test.crawler.api

import org.jsoup.nodes.Document

interface Selector<T> {

    fun select(document: Document): T

}