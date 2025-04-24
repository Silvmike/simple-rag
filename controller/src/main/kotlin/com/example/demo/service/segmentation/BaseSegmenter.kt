package com.example.demo.service.segmentation

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.api.DocumentSegment
import com.example.demo.service.api.SegmentedDocument
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter
import org.hibernate.sql.results.jdbc.internal.JdbcValuesMappingProducerProviderInitiator

class BaseSegmenter(
    private val maxChars: Int = 1024,
    private val maxOverlapChars: Int = 512
) : Segmenter {

    override fun split(domainDocument: DomainDocument): SegmentedDocument =
        SegmentedDocument(
            document = domainDocument,
            segments = doSplit(domainDocument)
        )

    private fun doSplit(domainDocument: DomainDocument) =
        DocumentByParagraphSplitter(maxChars, maxOverlapChars).split(
            dev.langchain4j.data.document.Document.from(domainDocument.content)
        ).map { textSegment ->
            DocumentSegment(
                fragment = textSegment.text(),
                startPos = 0,
                endPos = 0
            )
        }
}