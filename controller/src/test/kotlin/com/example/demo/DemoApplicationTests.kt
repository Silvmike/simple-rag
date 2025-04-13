package com.example.demo

import com.example.demo.app.DemoApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ActiveProfiles("STUB")
@ContextConfiguration(classes = [DemoApplication::class])
class DemoApplicationTests {

	@Test
	fun contextLoads() {
	}
}
