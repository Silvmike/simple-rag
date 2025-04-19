package ru.silvmike.test.crawler

import ru.silvmike.test.crawler.api.CrawlerSettings
import java.util.concurrent.ForkJoinPool

fun main() {

    val crawler = Crawler(
        settings = CrawlerSettings(
            startUrl = "https://russianfedora.github.io/FAQ/",
            maxDepth = 3,
            urlFilter = { pageUrl ->
                pageUrl.contains("russianfedora.github.io")
            }
        ) { page ->
            println(
                "url: ${page.url} has ${page.urls.size} links on it"
            )
        }
    )
    ForkJoinPool(4).invoke(crawler)
}