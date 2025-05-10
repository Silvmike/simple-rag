package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.containers.*
import com.example.demo.search_engines.yandex.YandexSearchClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(profiles = [
	TestProfiles.INTTEST
])
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [
		DatabaseContainer::class,
		VectorStoreContainer::class,
		EmbedContainer::class,
		DeepSeekContainer::class,
		SaigaContainer::class,
		RerankerContainer::class,
		MyOpenSearchContainer::class,
		SeleniumChromeContainer::class
	]
)
class DemoApplicationTests {

	@Autowired
	private lateinit var yandexSearchClient: YandexSearchClient

	@Test
	fun contextLoads() {

		println(yandexSearchClient.query("Кто такой винни-пух?"))

		CountDownLatch(1).await()
	}

}
