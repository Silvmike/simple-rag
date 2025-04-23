package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.app.Profiles
import com.example.demo.chat.api.MyChat
import com.example.demo.containers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(profiles = [TestProfiles.INTTEST, Profiles.DEEPSEEK, Profiles.FULL_TEXT])
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [
		DatabaseContainer::class,
		VectorStoreContainer::class,
		EmbedContainer::class,
		DeepSeekContainer::class,
		MyOpenSearchContainer::class
	]
)
class DemoApplicationTests {

	@Autowired
	lateinit var chatClient: MyChat

	@Test
	fun contextLoads() {

		println(chatClient.exchange("Привет"))

		CountDownLatch(1).await()
	}

}
