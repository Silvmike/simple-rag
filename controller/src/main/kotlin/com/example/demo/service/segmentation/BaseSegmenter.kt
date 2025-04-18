package com.example.demo.service.segmentation

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.api.DocumentSegment
import com.example.demo.service.api.SegmentedDocument
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter

class BaseSegmenter : Segmenter {

    override fun split(domainDocument: DomainDocument): SegmentedDocument =
        SegmentedDocument(
            document = domainDocument,
            segments = doSplit(domainDocument)
        )

    private fun doSplit(domainDocument: DomainDocument) =
        DocumentByParagraphSplitter(2048, 512).split(
            dev.langchain4j.data.document.Document.from(domainDocument.content)
        ).map { textSegment ->
            DocumentSegment(
                fragment = textSegment.text(),
                startPos = 0,
                endPos = 0
            )
        }
}