package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.chat.api.MyChat
import com.example.demo.containers.DatabaseContainer
import com.example.demo.containers.EmbedContainer
import com.example.demo.containers.VectorStoreContainer
import com.example.demo.service.store.UnsegmentedDocumentService
import org.junit.jupiter.api.Test
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.CountDownLatch

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(TestProfiles.INTTEST)
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [DatabaseContainer::class, VectorStoreContainer::class, EmbedContainer::class]
)
class DemoApplicationTests {

	@Autowired
	lateinit var unsegmentedDocumentService: UnsegmentedDocumentService

	@Autowired
	lateinit var vectorStore: QdrantVectorStore

	@Autowired
	lateinit var chatClient: MyChat

	@Test
	fun contextLoads() {
		CountDownLatch(1).await()
	}

}
