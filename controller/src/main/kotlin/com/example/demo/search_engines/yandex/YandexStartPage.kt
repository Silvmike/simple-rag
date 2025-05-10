package com.example.demo.search_engines.yandex

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class YandexStartPage private constructor(private val driver: WebDriver) {

    private val webDriverWait: WebDriverWait = WebDriverWait(driver, Duration.ofSeconds(30L))

    fun search(query: String): YandexSearchPage {
        driver.findElement(By.xpath("//*[@id=\"text\"]")).apply {
            sendKeys(query)
        }

        driver.findElement(By.cssSelector("button.search3__button")).apply {
            click()
        }
        webDriverWait.waitPageReadyState()
        return YandexSearchPage(driver)
    }

    companion object {
        @JvmStatic
        fun open(driver: WebDriver): YandexStartPage {
            driver.navigate().to("about:blank")
            val webDriverWait = WebDriverWait(driver, Duration.ofSeconds(30L))
            driver.get("https://ya.ru/")
            webDriverWait.waitPageReadyState()
            return YandexStartPage(driver)
        }
    }

}