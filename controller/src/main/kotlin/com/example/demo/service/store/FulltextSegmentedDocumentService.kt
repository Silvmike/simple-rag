package com.example.demo.service.store

import com.example.demo.opensearch.Indices
import com.example.demo.opensearch.document.Document
import com.example.demo.service.api.SegmentedDocument
import com.example.demo.service.store.api.CreateDocumentResponse
import com.example.demo.service.store.api.DocumentIdentifier
import com.example.demo.service.store.api.IdentifierType
import com.example.demo.service.store.api.SegmentedDocumentService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.opensearch.client.opensearch.OpenSearchClient
import java.util.*

class FulltextSegmentedDocumentService(
    private val client: OpenSearchClient
) : SegmentedDocumentService {

    private val mapper = ObjectMapper().registerModules(
        KotlinModule.Builder().build(), JavaTimeModule()
    )

    override fun createDocument(segmentedDocument: SegmentedDocument): CreateDocumentResponse {
        val documentId = UUID.randomUUID().toString()

        client.bulk { bulkBuilder ->
            bulkBuilder.index(Indices.DOCUMENTS.indexName)
                .operations { opBuilder ->
                    segmentedDocument.segments.forEach { segment ->
                        opBuilder.create { builder ->
                            builder.index(Indices.DOCUMENTS.indexName)
                                .document(
                                    mapper.writeValueAsString(
                                        Document(
                                            documentId = documentId,
                                            content = segment.fragment
                                        )
                                    )
                                )
                        }
                    }
                    opBuilder
                }
        }

        return CreateDocumentResponse(
            ids = listOf(
                DocumentIdentifier(
                    id = documentId,
                    identifierType = IdentifierType.FULLTEXT
                )
            )
        )
    }

    override fun deleteDocument(documentId: DocumentIdentifier) {
        check(supportsIdentifier(documentId.identifierType)) {
            "${documentId.identifierType} is not supported!"
        }
        client.deleteByQuery { builder ->
            builder.index(Indices.DOCUMENTS.indexName)
                .query { qBuilder ->
                    qBuilder.bool { boolBuilder ->
                        boolBuilder.must { mustBuilder ->
                            mustBuilder.match { matchBuilder ->
                                matchBuilder
                                    .field("document_id")
                                    .query { matchQueryBuilder ->
                                        matchQueryBuilder
                                            .stringValue(documentId.id)
                                    }
                            }
                        }
                    }
                }

        }
    }

    override fun supportsIdentifier(identifierType: IdentifierType): Boolean =
        identifierType == IdentifierType.FULLTEXT

}