package com.example.demo.service.query.advice

import com.example.demo.chat.api.MyChat
import com.example.demo.util.loadResourceDocument
import com.example.demo.util.template.TemplateProvider
import com.google.common.base.Suppliers
import dev.langchain4j.model.input.PromptTemplate
import org.slf4j.LoggerFactory
import java.io.StringReader

private const val OPTIMIZED_QUERY_PREFIX = "Оптимизированный запрос: "
private const val REASON_PREFIX = "Пояснение: "

class MyChatPromptAdviceQueryEnricher(
    private val chat: MyChat,
    private val promptTemplateProvider: TemplateProvider
) : QueryEnricher {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun enrich(query: String): String {
        val promptDoc = promptTemplateProvider.get()!!
        val generatedRequest = PromptTemplate.from(promptDoc.text())
            .apply(mapOf("query" to query)).text()

        logger.info("Generated request: $generatedRequest")

        StringReader(chat.exchange(generatedRequest)).use {
            val lines = it.readLines()

            val suggestion = lines.filter { line ->
                line.startsWith(OPTIMIZED_QUERY_PREFIX)
            }.firstOrNull()?.replace(OPTIMIZED_QUERY_PREFIX, "") ?: query

            val reason = lines.filter { line ->
                line.startsWith(REASON_PREFIX)
            }.firstOrNull()?.replace(REASON_PREFIX, "") ?: ""

            logger.info(
                "[FULLTEXT OPTIMIZER] Query: [{}], \n\tOptimized query: [{}], \n\tReason: [{}]",
                query,
                suggestion,
                reason
            )

            return suggestion.replace("\"", "")
        }
    }
}