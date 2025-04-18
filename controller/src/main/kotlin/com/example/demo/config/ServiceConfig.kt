package com.example.demo.config

import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.service.api.TxService
import com.example.demo.service.segmentation.BaseSegmenter
import com.example.demo.service.segmentation.Segmenter
import com.example.demo.service.store.SegmentedDocumentService
import com.example.demo.service.store.UnsegmentedDocumentService
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(DbConfig::class)
@Configuration
class ServiceConfig {
    
    @Bean
    fun segmentedDocumentService(
        vectoreStore: VectorStore,
        documentSegmentDao: DocumentSegmentDao,
        documentDao: DocumentDao,
        txService: TxService
    ) = SegmentedDocumentService(vectoreStore, documentSegmentDao, documentDao, txService)

    @Bean
    fun unsegmentedDocumentService(
        segmenter: Segmenter,
        segmentedDocumentService: SegmentedDocumentService
    ) = UnsegmentedDocumentService(segmenter, segmentedDocumentService)

    @Bean
    fun segmenter() = BaseSegmenter()
}