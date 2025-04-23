package com.example.demo.service.query

import com.example.demo.opensearch.Indices
import com.example.demo.opensearch.document.DocumentIndexData
import com.example.demo.service.query.api.SimilaritySearchService
import org.opensearch.client.opensearch.OpenSearchClient
import org.springframework.core.Ordered

class FullTextSimilaritySearchService(
    private val client: OpenSearchClient,
    private val topK: Int = 5
) : SimilaritySearchService, Ordered {

    override fun search(query: String): List<String> =
        client.search({ searchBuilder ->
            searchBuilder
                .size(topK)
                .index(listOf(Indices.DOCUMENTS.indexName))
                .query { queryBuilder ->
                    queryBuilder
                        .bool { boolBuilder ->
                            boolBuilder.must { mustBuilder ->
                                mustBuilder.simpleQueryString { queryString ->
                                    queryString.query(query)
                                }
                            }

                        }
                }
        }, DocumentIndexData::class.java).hits().hits().map { it.source()!!.content!! }

    override fun getOrder(): Int = 1
}