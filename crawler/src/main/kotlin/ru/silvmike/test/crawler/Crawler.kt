package ru.silvmike.test.crawler

import org.jsoup.Jsoup
import ru.silvmike.test.crawler.api.CrawlerSettings
import ru.silvmike.test.crawler.api.ParsedPage
import ru.silvmike.test.crawler.util.UnsafeClientFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.RecursiveTask

class Crawler : RecursiveTask<List<ParsedPage>> {

    private val depth: Int
    private val visited: ConcurrentHashMap<String, String>
    private val settings: CrawlerSettings

    constructor(settings: CrawlerSettings) {
        this.settings = settings
        this.depth = 0
        this.visited = ConcurrentHashMap()
    }

    private constructor(
        settings: CrawlerSettings,
        depth: Int,
        visited: ConcurrentHashMap<String, String>
    ) {
        this.settings = settings
        this.depth = depth
        this.visited = visited
    }

    override fun compute(): List<ParsedPage> {
        return try {
            val page = parsePage(settings.startUrl)
            settings.resultListener.onPageParsed(page)

            if (depth >= settings.maxDepth) {
                listOf(page)
            } else {
                val result = mutableListOf(page)
                val crawlers = page.urls
                    .asSequence()
                    .filter { url -> visited.putIfAbsent(url, url) == null }
                    .map { url ->
                        Crawler(
                            copySettingsWithUrl(url),
                            depth + 1,
                            visited
                        )
                    }
                crawlers
                    .map { it.fork() }
                    .map { it.join() }
                    .forEach {
                        result += it
                    }

                result
            }
        } catch (e: Exception) {
            println("Failed to download ${settings.startUrl}")
            emptyList<ParsedPage>()
        }
    }

    private fun copySettingsWithUrl(url: String) = CrawlerSettings(
        startUrl = url,
        bodySelector = settings.bodySelector,
        urlSelector = settings.urlSelector,
        resultListener = settings.resultListener,
        maxDepth = settings.maxDepth,
        timeout = settings.timeout,
        urlFilter = settings.urlFilter
    )

    private fun parsePage(url: String): ParsedPage =
        Jsoup.connect(url)
            .sslSocketFactory(UnsafeClientFactory.sslContext.socketFactory)
            .timeout(settings.timeout)
            .get().let {
                ParsedPage(
                    url = url,
                    urls =
                        settings.urlSelector.select(it)
                            .filter { pageUrl -> !pageUrl.startsWith("#") }
                            .map { pageUrl ->
                                if (!pageUrl.startsWith("http:") && !pageUrl.startsWith("https:")) {
                                    "${getBaseUrl(url)}/$pageUrl"
                                } else {
                                    pageUrl
                                }
                            }
                            .map { pageUrl ->
                                pageUrl.indexOf('#').let { index ->
                                    if (index != -1) {
                                        pageUrl.substring(0, index)
                                    } else {
                                        pageUrl
                                    }
                                }
                            }
                            .filter { pageUrl -> settings.urlFilter.test(pageUrl) }
                    ,
                    meaningfulContent = settings.bodySelector.select(it)
                )
            }

    private fun getBaseUrl(url: String): String =
        url.substring(0, url.lastIndexOf('/'))

}