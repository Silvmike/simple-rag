package com.example.demo

import com.example.demo.app.DemoApplication
import com.example.demo.containers.DatabaseContainer
import com.example.demo.containers.VectorStoreContainer
import com.example.demo.dao.DocumentDao
import com.example.demo.entity.Document
import com.example.demo.service.api.TxService
import org.junit.ClassRule
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ActiveProfiles(TestProfiles.INTTEST)
@ContextConfiguration(
	classes = [DemoApplication::class],
	initializers = [DatabaseContainer::class, VectorStoreContainer::class]
)
class DemoApplicationTests {

	@Autowired
	lateinit var documentDao: DocumentDao

	@Autowired
	lateinit var txService: TxService

	@Test
	fun contextLoads() {

		txService.execute {
			var doc = Document(doc = "docododod")
			doc = documentDao.save(doc)
			println(doc.id)
		}
	}

}
