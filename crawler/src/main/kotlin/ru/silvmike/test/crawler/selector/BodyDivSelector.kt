package ru.silvmike.test.crawler.selector

import org.jsoup.nodes.Document
import ru.silvmike.test.crawler.api.Selector

object BodyDivSelector : Selector<String> {

    override fun select(document: Document): String =
        document.select("div.body").html()
}