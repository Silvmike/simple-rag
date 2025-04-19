package ru.silvmike.test.crawler.selector

import org.jsoup.nodes.Document
import ru.silvmike.test.crawler.api.Selector

object UrlSelector : Selector<List<String>> {

    override fun select(document: Document): List<String> =
        document
            .select("div.body")
            .select("a")
            .eachAttr("href")
}