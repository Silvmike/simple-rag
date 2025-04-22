package ru.silvmike.test.crawler.selector

import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.silvmike.test.crawler.api.Selector

object SectionBodyDivSelector : Selector<List<String>> {

    override fun select(document: Document): List<String> =
        document.select("div.body").select("section").map { it.html() }
}