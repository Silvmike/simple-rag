package com.example.demo.service.api

data class SegmentedDocument(
    val document: DomainDocument,
    val segments: List<DocumentSegment>
)