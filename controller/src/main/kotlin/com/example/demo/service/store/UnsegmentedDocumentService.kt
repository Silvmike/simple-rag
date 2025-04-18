package com.example.demo.service.store

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.segmentation.Segmenter

class UnsegmentedDocumentService(
    private val segmenter: Segmenter,
    private val segmentedDocumentService: SegmentedDocumentService
) {

    fun createDocument(domainDocument: DomainDocument): Long =
        segmentedDocumentService.createDocument(
            segmenter.split(domainDocument)
        )

    fun deleteDocument(documentId: Long) {
        segmentedDocumentService.deleteDocument(documentId)
    }
}