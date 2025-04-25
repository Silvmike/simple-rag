package com.example.demo.service.store.api

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.dto.IdentifierType

interface UnsegmentedDocumentService {

    fun createDocument(domainDocument: DomainDocument): CreateDocumentResponse

    fun deleteDocument(documentId: DocumentIdentifier)

    fun supportsIdentifier(identifierType: IdentifierType): Boolean
}