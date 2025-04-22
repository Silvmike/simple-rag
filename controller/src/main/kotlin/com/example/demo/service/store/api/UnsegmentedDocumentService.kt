package com.example.demo.service.store.api

import com.example.demo.service.api.DomainDocument

interface UnsegmentedDocumentService {

    fun createDocument(domainDocument: DomainDocument): CreateDocumentResponse

    fun deleteDocument(documentId: DocumentIdentifier)

    fun supportsIdentifier(identifierType: IdentifierType): Boolean
}