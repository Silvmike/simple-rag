package com.example.demo.service.store

import com.example.demo.opensearch.Indices
import com.example.demo.opensearch.document.DocumentIndexData
import com.example.demo.service.api.SegmentedDocument
import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.dto.IdentifierType
import com.example.demo.service.store.api.SegmentedDocumentService
import org.opensearch.client.opensearch.OpenSearchClient
import java.util.*

class FulltextSegmentedDocumentService(
    private val client: OpenSearchClient
) : SegmentedDocumentService {

    override fun createDocument(segmentedDocument: SegmentedDocument): CreateDocumentResponse {
        val documentId = UUID.randomUUID().toString()

        client.bulk { bulkBuilder ->
            bulkBuilder.index(Indices.DOCUMENTS.indexName)
                .operations { opBuilder ->
                    segmentedDocument.segments.forEach { segment ->
                        opBuilder.create { builder ->
                            builder.index(Indices.DOCUMENTS.indexName)
                                .document(
                                    DocumentIndexData(
                                        document_id = documentId,
                                        content = segment.fragment
                                    )
                                )
                        }
                    };
                    return@operations opBuilder
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