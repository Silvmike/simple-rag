package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.chat.api.MyChat
import com.example.demo.chat.giga.api.GigaChatClient
import com.example.demo.containers.DatabaseContainer
import com.example.demo.containers.EmbedContainer
import com.example.demo.containers.VectorStoreContainer
import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.entity.Document
import com.example.demo.entity.DocumentSegment
import com.example.demo.service.api.TxService
import org.junit.jupiter.api.Test
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ActiveProfiles(TestProfiles.INTTEST)
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [DatabaseContainer::class, VectorStoreContainer::class, EmbedContainer::class]
)
class DemoApplicationTests {

	@Autowired
	lateinit var documentDao: DocumentDao

	@Autowired
	lateinit var documentSegmentDao: DocumentSegmentDao

	@Autowired
	lateinit var txService: TxService

	@Autowired
	lateinit var vectorStore: QdrantVectorStore

	@Autowired
	lateinit var chatClient: MyChat

	@Test
	fun contextLoads() {

		val id = txService.execute {
			var doc = Document(doc = "docododod")
			doc = documentDao.save(doc)

			val fragment = documentSegmentDao.save(DocumentSegment(
				fragment = "doc",
				document = doc
			))

			doc.id
		}

		vectorStore.add(
			listOf(
				org.springframework.ai.document.Document(
					"У лукоморья дуб зелёный " +
							"Златая цепь на дубе том " +
							"И днём и ночью кот учёный " +
							"Всё ходит по цепи кругом " +
							"Идёт направо песнь заводит " +
							"Налево сказку говорит".lowercase(),
					mutableMapOf<String, Any?>(
						"_id" to id.toString(),
					)
				)
			)
		)

		println(vectorStore.similaritySearch("цепь на дубе"))

		println(chatClient.exchange("Come up with 5 famous pirates' names"))
	}

}
