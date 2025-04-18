package com.example.demo.service.query

import com.example.demo.chat.api.MyChat
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.model.input.PromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import java.nio.file.Paths

class QueryService(
    private val vectorStore: VectorStore,
    private val chat: MyChat
) {

    fun query(query: String): String {
        val docs = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query.lowercase())
                .topK(5)
                .build()
        )!!.map { it.text!! }

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

        println("Generated request: $generatedRequest")

        return chat.exchange(
            generatedRequest
        )
    }

    private fun toSources(docs: List<String>): String =
        docs.joinToString(separator = "\n===\n")

}