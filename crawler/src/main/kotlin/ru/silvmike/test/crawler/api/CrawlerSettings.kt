package ru.silvmike.test.crawler.api

import ru.silvmike.test.crawler.selector.SectionBodyDivSelector
import ru.silvmike.test.crawler.selector.UrlSelector
import java.util.function.Predicate

data class CrawlerSettings(
    val startUrl: String,
    val maxDepth: Int,
    val timeout: Int = 15000,
    val contentSelector: Selector<List<String>> = SectionBodyDivSelector,
    val urlSelector: Selector<List<String>> = UrlSelector,
    val urlFilter: Predicate<String>,
    val resultListener: ResultListener
)