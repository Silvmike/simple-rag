package com.example.demo.service.store

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.segmentation.Segmenter
import com.example.demo.service.store.api.*
import com.example.demo.service.store.api.UnsegmentedDocumentService
import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.dto.IdentifierType

class UnsegmentedDocumentServiceImpl(
    private val segmenter: Segmenter,
    private val segmentedDocumentService: SegmentedDocumentService
) : UnsegmentedDocumentService {

    override fun createDocument(domainDocument: DomainDocument): CreateDocumentResponse =
        segmentedDocumentService.createDocument(
            segmenter.split(domainDocument)
        )

    override fun deleteDocument(documentId: DocumentIdentifier) {
        check(supportsIdentifier(documentId.identifierType)) {
            "${documentId.identifierType} is not supported!"
        }
        segmentedDocumentService.deleteDocument(documentId)
    }

    override fun supportsIdentifier(identifierType: IdentifierType): Boolean =
        segmentedDocumentService.supportsIdentifier(identifierType)
}