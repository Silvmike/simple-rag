package com.example.demo.service.store.api

import com.example.demo.service.api.SegmentedDocument
import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.dto.IdentifierType

interface SegmentedDocumentService {

    fun createDocument(segmentedDocument: SegmentedDocument): CreateDocumentResponse

    fun deleteDocument(documentId: DocumentIdentifier)

    fun supportsIdentifier(identifierType: IdentifierType): Boolean
}