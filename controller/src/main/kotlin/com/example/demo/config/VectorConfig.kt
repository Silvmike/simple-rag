package com.example.demo.config

import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.service.api.TxService
import com.example.demo.service.segmentation.BaseSegmenter
import com.example.demo.service.segmentation.Segmenter
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import com.example.demo.service.store.VectorSegmentedDocumentService
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VectorConfig {

    @Bean
    fun segmentedDocumentService(
        vectoreStore: VectorStore,
        documentSegmentDao: DocumentSegmentDao,
        documentDao: DocumentDao,
        txService: TxService
    ) = VectorSegmentedDocumentService(vectoreStore, documentSegmentDao, documentDao, txService)

    @Bean
    fun unsegmentedDocumentService(
        segmentedDocumentService: VectorSegmentedDocumentService
    ) = UnsegmentedDocumentServiceImpl(BaseSegmenter(maxChars = 2048, maxOverlapChars = 32), segmentedDocumentService)

}