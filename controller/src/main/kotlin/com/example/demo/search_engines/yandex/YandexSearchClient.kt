package com.example.demo.search_engines.yandex

import com.example.demo.config.properties.Options
import com.google.common.base.Suppliers
import okio.withLock
import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.time.Duration
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Supplier

/**
 * For the sake of demo.
 * TODO implement similarity service based on the client.
 */
class YandexSearchClient(
    options: Options
) {

    private val mutex = ReentrantLock()
    private val closeable: Boolean

    init {
        if (options.yandexSearchClient.driver.location != null) {
            System.setProperty("webdriver.chrome.driver", options.yandexSearchClient.driver.location!!)
        }
        closeable = options.yandexSearchClient.driver.local
    }

    private val driverSupplier: Supplier<WebDriver> =
        if (options.yandexSearchClient.driver.local) {
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
                if (options.yandexSearchClient.driver.headless) {
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
                    .address(options.yandexSearchClient.driver.hubUrl)
                    .build().apply {
                        manage().window().setSize(Dimension(1600, 1200))
                    }
            }
        }

    fun query(query: String): List<SearchResult> {
        mutex.withLock {
            val driver = driverSupplier.get()
            try {
                val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(30L))
                driver.navigate().to("about:blank")

                driver.get("https://ya.ru/")
                webDriverWait.waitPageReadyState()

                driver.findElement(By.xpath("//*[@id=\"text\"]")).apply {
                    sendKeys(query)
                }

                driver.findElement(By.cssSelector("button.search3__button")).apply {
                    click()
                }
                webDriverWait.waitPageReadyState()

                return driver.findElements(By.xpath("//*[@id=\"search-result\"]/li[@data-fast=\"1\"]"))
                    .filter { searchResultListItem -> searchResultListItem.getAttribute("data-fast-name") == null }
                    .map { searchResultListItem ->
                        val document = Jsoup.parse(searchResultListItem.getAttribute("innerHTML")!!)
                        val url = document.select("div.organic__title-wrapper > a:nth-child(1)").attr("href")
                        val shortDescription =
                            document.select("div.organic__content-wrapper > div.organic__text").text()
                        SearchResult(
                            url = url,
                            shortDescription = shortDescription
                        )
                    }
            } finally {
                if (closeable) {
                    driver.close()
                }
            }
        }
    }

    private fun WebDriverWait.waitPageReadyState() {
        until { webDriver: WebDriver ->
            try {
                val ready =
                    (webDriver as JavascriptExecutor).executeScript("return !!document['readyState'];") as Boolean

                if (ready) {
                    webDriver.findElements(By.className("search3__input-inner-container")).stream()
                        .forEach { div: WebElement -> div.isDisplayed }
                    true
                } else {
                    false
                }
            } catch (e: WebDriverException) {
                false
            }
        }
    }

}