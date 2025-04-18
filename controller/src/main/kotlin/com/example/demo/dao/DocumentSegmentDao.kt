package com.example.demo.dao

import com.example.demo.entity.DocumentSegmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentSegmentDao : JpaRepository<DocumentSegmentEntity, Long> {

    fun findByDocumentId(documentId: Long): List<DocumentSegmentEntity>

}