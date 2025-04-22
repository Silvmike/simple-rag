package com.example.demo.service.query

import com.example.demo.chat.api.MyChat
import com.example.demo.service.query.api.SimilaritySearchService
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.model.input.PromptTemplate
import org.slf4j.LoggerFactory
import java.nio.file.Paths

class QueryService(
    private val similaritySearchService: SimilaritySearchService,
    private val chat: MyChat
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun query(query: String): String {
        val docs = similaritySearchService.search(query)

        val promptDoc = FileSystemDocumentLoader.loadDocument(
            Paths.get(
                javaClass.getResource(
                    "/prompt/template/query.txt"
                )?.toURI() ?: throw RuntimeException("file not found!")
            )
        )

        val generatedRequest = PromptTemplate.from(promptDoc.text())
            .apply(
                mapOf("query" to query, "context" to toSources(docs))
            ).text()

        logger.info("Generated request: $generatedRequest")

        return chat.exchange(
            generatedRequest
        )
    }

    private fun toSources(docs: List<String>): String =
        docs.joinToString(separator = "\n===\n")

}