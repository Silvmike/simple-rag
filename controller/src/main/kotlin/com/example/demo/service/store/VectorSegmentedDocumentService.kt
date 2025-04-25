package com.example.demo.service.store

import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.entity.DocumentEntity
import com.example.demo.entity.DocumentSegmentEntity
import com.example.demo.service.api.SegmentedDocument
import com.example.demo.util.tx.TxService
import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.dto.IdentifierType
import com.example.demo.service.store.api.SegmentedDocumentService
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore

class VectorSegmentedDocumentService(
    private val vectoreStore: VectorStore,
    private val documentSegmentDao: DocumentSegmentDao,
    private val documentDao: DocumentDao,
    private val txService: TxService
) : SegmentedDocumentService {

    override fun createDocument(segmentedDocument: SegmentedDocument): CreateDocumentResponse {

        val documentEntity = DocumentEntity(
            content = segmentedDocument.document.content
        )
        val segments = segmentedDocument.segments.map {
            DocumentSegmentEntity(
                fragment = it.fragment,
                startPos = it.startPos,
                endPos = it.endPos,
                document = documentEntity
            )
        }

        return txService.execute {
            documentDao.save(documentEntity)

            val segmentMap =
            segments.associateBy(
                { segment -> segment },
                { segment ->
                    Document(
                        segment.fragment!!,
                        metadata(segment)
                    )
                }
            )

            vectoreStore.add(segmentMap.values.toList())
            segmentMap.forEach { (segment, document) ->
                segment.qdrandtId = document.id
            }

            documentSegmentDao.saveAll(segments)

            CreateDocumentResponse(
                ids = listOf(DocumentIdentifier(
                    id = documentEntity.id.toString(),
                    identifierType = IdentifierType.VECTOR
                ))
            )
        }
    }

    private fun metadata(segment: DocumentSegmentEntity): MutableMap<String, Any> =
        mutableMapOf(
            "start_pos" to segment.startPos.toString(),
            "end_pos" to segment.endPos.toString(),
            "segment_id" to segment.id.toString(),
            "doc_id" to segment.document!!.id.toString()
        )

    override fun deleteDocument(documentId: DocumentIdentifier) {
        check(supportsIdentifier(documentId.identifierType)) {
            "${documentId.identifierType} is not supported!"
        }

        txService.execute {
            val document = documentDao.findById(documentId.id.toLong()).get()
            val segments = documentSegmentDao.findByDocumentId(documentId.id.toLong())

            vectoreStore.delete(
                segments.map { it.qdrandtId }
            )
            documentSegmentDao.deleteAll(segments)
            documentDao.delete(document)
        }
    }

    override fun supportsIdentifier(identifierType: IdentifierType): Boolean =
        identifierType == IdentifierType.VECTOR

}