package com.example.demo.service.store

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.store.api.CreateDocumentResponse
import com.example.demo.service.store.api.DocumentIdentifier
import com.example.demo.service.store.api.IdentifierType
import com.example.demo.service.store.api.UnsegmentedDocumentService

class ComplexUnsegmentedDocumentService(
    val chain: List<UnsegmentedDocumentService>
) : UnsegmentedDocumentService {

    override fun createDocument(domainDocument: DomainDocument): CreateDocumentResponse =
        CreateDocumentResponse(
            ids = chain.asSequence()
                .flatMap { it.createDocument(domainDocument).ids.asSequence() }
                .toList()
        )

    override fun deleteDocument(documentId: DocumentIdentifier) {
        chain.find { it.supportsIdentifier(documentId.identifierType) }?.deleteDocument(documentId)
    }

    override fun supportsIdentifier(identifierType: IdentifierType): Boolean =
        chain.any { it.supportsIdentifier(identifierType) }

}