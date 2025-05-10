package com.example.demo.search_engines.yandex

import org.openqa.selenium.*
import org.openqa.selenium.support.ui.WebDriverWait

fun WebDriverWait.waitPageReadyState() {
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