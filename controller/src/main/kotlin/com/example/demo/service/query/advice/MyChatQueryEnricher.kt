package com.example.demo.service.query.advice

import com.example.demo.chat.api.MyChat
import com.google.common.base.Suppliers
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.model.input.PromptTemplate
import org.slf4j.LoggerFactory
import java.nio.file.Paths

class MyChatQueryEnricher(
    private val chat: MyChat,
) : QueryEnricher {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val promptDocSupplier = Suppliers.memoize {
        FileSystemDocumentLoader.loadDocument(
            Paths.get(
                javaClass.getResource(
                    "/prompt/template/fulltext_advice.txt"
                )?.toURI() ?: throw RuntimeException("file not found!")
            )
        )
    }

    override fun enrich(query: String): String {
        val promptDoc = promptDocSupplier.get()
        val generatedRequest = PromptTemplate.from(promptDoc.text())
            .apply(mapOf("query" to query)).text()

        logger.info("Generated request: $generatedRequest")

        return chat.exchange(generatedRequest)
    }
}