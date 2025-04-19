package com.example.demo.service.transform

import com.example.demo.service.api.DomainDocument
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer

object HtmlDocumentFactory {

    fun transform(content: String) =
        DomainDocument(
            content = HtmlToTextDocumentTransformer().transform(
                Document.from(content)
            ).text().lowercase()
        )
}