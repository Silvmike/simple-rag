package com.example.demo.service.store

import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.entity.DocumentEntity
import com.example.demo.entity.DocumentSegmentEntity
import com.example.demo.service.api.SegmentedDocument
import com.example.demo.service.api.TxService
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore

class SegmentedDocumentService(
    private val vectoreStore: VectorStore,
    private val documentSegmentDao: DocumentSegmentDao,
    private val documentDao: DocumentDao,
    private val txService: TxService
) {

    fun createDocument(segmentedDocument: SegmentedDocument): Long {

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

            documentEntity.id!!
        }
    }

    private fun metadata(segment: DocumentSegmentEntity): MutableMap<String, Any> =
        mutableMapOf(
            "start_pos" to segment.startPos.toString(),
            "end_pos" to segment.endPos.toString(),
            "segment_id" to segment.id.toString(),
            "doc_id" to segment.document!!.id.toString()
        )

    fun deleteDocument(documentId: Long) {
        txService.execute {
            val document = documentDao.findById(documentId).get()
            val segments = documentSegmentDao.findByDocumentId(documentId)

            vectoreStore.delete(
                segments.map { it.qdrandtId }
            )
            documentSegmentDao.deleteAll(segments)
            documentDao.delete(document)
        }
    }

}