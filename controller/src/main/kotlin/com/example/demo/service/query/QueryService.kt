package com.example.demo.service.query

import com.example.demo.chat.api.MyChat
import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.util.loadResourceDocument
import com.google.common.base.Suppliers
import dev.langchain4j.model.input.PromptTemplate
import org.slf4j.LoggerFactory

class QueryService(
    private val similaritySearchService: SimilaritySearchService,
    private val chat: MyChat
) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val promptDocSupplier = Suppliers.memoize {
        "/prompt/template/query.txt".loadResourceDocument()
    }

    fun query(query: String): String {
        val docs = similaritySearchService.search(query)

        val promptDoc = promptDocSupplier.get()
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