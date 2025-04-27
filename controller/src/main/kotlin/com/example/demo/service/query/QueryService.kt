package com.example.demo.service.query

import com.example.demo.chat.api.MyChat
import com.example.demo.parameters.ApplicationParameters
import com.example.demo.reranker.api.RerankRequest
import com.example.demo.reranker.api.RerankerClient
import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.util.template.TemplateProvider
import dev.langchain4j.model.input.PromptTemplate
import org.slf4j.LoggerFactory

class QueryService(
    private val similaritySearchService: SimilaritySearchService,
    private val reranker: RerankerClient,
    private val chat: MyChat,
    private val queryPromptTemplateProvider: TemplateProvider,
    private val applicationParameters: ApplicationParameters
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun query(query: String): String {
        val docs = rerank(
            query = query,
            foundDocs = similaritySearchService.search(query),
            topK = applicationParameters.rerankerMaxResults
        )

        val promptDoc = queryPromptTemplateProvider.get()!!
        val generatedRequest = PromptTemplate.from(promptDoc.text())
            .apply(
                mapOf("query" to query, "context" to toSources(docs))
            ).text()

        logger.info("Generated request: $generatedRequest")

        return chat.exchange(
            generatedRequest
        )
    }

    private fun rerank(
        query: String,
        foundDocs: List<String>,
        topK: Int
    ) = reranker.rerank(
        RerankRequest(
            query = query,
            documents = foundDocs,
            topK = topK,
        )
    ).documents.map { it.document }

    private fun toSources(docs: List<String>): String =
        docs.joinToString(separator = "\n===\n")

}