package com.example.demo.search_engines.yandex

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class YandexSearchPage(private val driver: WebDriver) {

    fun getResults(): List<SearchResult> =
        driver.findElements(By.xpath("//*[@id=\"search-result\"]/li[@data-fast=\"1\"]"))
            .filter { searchResultListItem -> isNormalTextSearchResult(searchResultListItem) }
            .map { searchResultListItem ->
                val document = Jsoup.parse(searchResultListItem.getAttribute("innerHTML")!!)
                extractSearchResult(document)
            }

    fun newSearch(): YandexStartPage =
        YandexStartPage.open(driver)

    private fun extractSearchResult(document: Document): SearchResult {
        val url = document.select("div.organic__title-wrapper > a:nth-child(1)").attr("href")
        val shortDescription =
            document.select("div.organic__content-wrapper > div.organic__text").text()
        return SearchResult(
            url = url,
            shortDescription = shortDescription
        )
    }

    private fun isNormalTextSearchResult(searchResultListItem: WebElement) =
        searchResultListItem.getAttribute("data-fast-name") == null
}