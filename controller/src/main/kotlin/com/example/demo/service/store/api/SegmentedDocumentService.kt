package com.example.demo.service.store.api

import com.example.demo.service.api.SegmentedDocument

interface SegmentedDocumentService {

    fun createDocument(segmentedDocument: SegmentedDocument): CreateDocumentResponse

    fun deleteDocument(documentId: DocumentIdentifier)

    fun supportsIdentifier(identifierType: IdentifierType): Boolean
}