package com.example.demo.search_engines.yandex

import com.example.demo.config.properties.Options
import com.example.demo.search_engines.api.SearchEngineClient
import com.example.demo.search_engines.api.SearchResult
import com.google.common.base.Suppliers
import okio.withLock
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Supplier

/**
 * For the sake of demo: doesn't pass captcha protection.
 * TODO implement similarity service based on the client.
 */
class YandexSearchEngineClient(
    options: Options
): SearchEngineClient {

    private val mutex = ReentrantLock()
    private val closeable: Boolean

    init {
        if (options.searchEngine.yandexSearchClient.driver.location != null) {
            System.setProperty("webdriver.chrome.driver", options.searchEngine.yandexSearchClient.driver.location!!)
        }
        closeable = options.searchEngine.yandexSearchClient.driver.local
    }

    private val driverSupplier: Supplier<WebDriver> =
        if (options.searchEngine.yandexSearchClient.driver.local) {
            Supplier {
                val chromeOptions = ChromeOptions()
                val arguments = mutableListOf(
                    "--no-sandbox",
                    "--no-first-run",
                    "--disable-translate",
                    "--disable-extensions",
                    "--disable-default-apps",
                    "--disable-sync",
                    "--disable-background-networking",
                    "--disable-setuid-sandbox",
                    "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36"
                )
                if (options.searchEngine.yandexSearchClient.driver.headless) {
                    arguments.add("--headless")
                }
                chromeOptions.addArguments(arguments)
                ChromeDriver(chromeOptions).apply {
                    manage().window().setSize(Dimension(1600, 1200))
                }
            }
        } else {
            Suppliers.memoize {
                ChromeDriver.builder()
                    .address(options.searchEngine.yandexSearchClient.driver.hubUrl)
                    .build().apply {
                        manage().window().setSize(Dimension(1600, 1200))
                    }
            }
        }

    override fun query(query: String): List<SearchResult> {
        mutex.withLock {
            val driver = driverSupplier.get()
            try {
                return YandexStartPage.open(driver)
                    .search(query)
                    .getResults()
            } finally {
                if (closeable) {
                    driver.close()
                }
            }
        }
    }

}