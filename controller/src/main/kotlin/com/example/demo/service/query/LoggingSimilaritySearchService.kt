package com.example.demo.service.query

import com.example.demo.service.query.api.SimilaritySearchService
import com.example.demo.util.LazyStringifier
import org.slf4j.LoggerFactory

class LoggingSimilaritySearchService(
    private val delegate: SimilaritySearchService
) : SimilaritySearchService {

    private val logger = LoggerFactory.getLogger(javaClass)
    override fun search(query: String): List<String> =
        try {
            delegate.search(query).apply {
                logger.info(
                    "For [{}] found [{}] results [{}]",
                    query,
                    size,
                    LazyStringifier(this)
                )
            }
        } catch (e: Exception) {
            logger.error("Error while searching for [$query]", e)
            throw e
        }

}