package com.example.demo.config

import com.example.demo.app.Profiles
import com.example.demo.dao.DocumentDao
import com.example.demo.dao.DocumentSegmentDao
import com.example.demo.parameters.ApplicationParameters
import com.example.demo.util.tx.TxService
import com.example.demo.service.query.LoggingSimilaritySearchService
import com.example.demo.service.query.VectorStoreSimilaritySearchService
import com.example.demo.service.segmentation.BaseSegmenter
import com.example.demo.service.store.UnsegmentedDocumentServiceImpl
import com.example.demo.service.store.VectorSegmentedDocumentService
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile(Profiles.VECTOR)
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

    @Bean
    fun vectorSimilaritySearchService(vectorStore: VectorStore, params: ApplicationParameters) =
        LoggingSimilaritySearchService(VectorStoreSimilaritySearchService(vectorStore, params))

}