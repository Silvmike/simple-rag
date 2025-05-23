package ru.silvmike.test.crawler

import ru.silvmike.test.crawler.api.CrawlerSettings
import ru.silvmike.test.crawler.digest.DigestClient
import ru.silvmike.test.crawler.util.UnsafeOkHttpClientFactory
import java.util.concurrent.ForkJoinPool

fun main() {

    val client = DigestClient(
        UnsafeOkHttpClientFactory.create()
    )

    val crawler = Crawler(
        settings = CrawlerSettings(
            startUrl = "https://russianfedora.github.io/FAQ/",
            maxDepth = 3,
            urlFilter = { pageUrl ->
                pageUrl.contains("russianfedora.github.io")
            }
        ) { page ->
            page.meaningfulContent!!.forEach { content ->
                client.digest(content)
            }
            println(
                "url: ${page.url} has ${page.meaningfulContent.size} meaningful extracts and ${page.urls.size} links on it"
            )
        }
    )
    ForkJoinPool(4).invoke(crawler)
}