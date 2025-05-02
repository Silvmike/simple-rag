package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.app.Profiles
import com.example.demo.containers.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(profiles = [
	TestProfiles.INTTEST,
	Profiles.OLLAMA_CHAT,
	Profiles.OLLAMA_SAIGA,
	//Profiles.DEEPSEEK,
	//Profiles.OLLAMA_DEEPSEEK,
	//Profiles.GIGACHAT,
	Profiles.FULL_TEXT,
	Profiles.TEST_CHAT,
	Profiles.VECTOR
])
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [
		DatabaseContainer::class,
		VectorStoreContainer::class,
		EmbedContainer::class,
		//DeepSeekContainer::class,
		SaigaContainer::class,
		RerankerContainer::class,
		MyOpenSearchContainer::class
	]
)
class DemoApplicationTests {

	@Test
	fun contextLoads() {
		CountDownLatch(1).await()
	}

}
