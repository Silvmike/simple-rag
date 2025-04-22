package com.example.demo.service.query

import com.example.demo.opensearch.Indices
import com.example.demo.service.query.api.SimilaritySearchService
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.SearchType

class FullTextSimilaritySearchService(
    private val client: OpenSearchClient
) : SimilaritySearchService {

    override fun search(query: String): List<String> =
        client.search({ searchBuilder ->
            searchBuilder
                .index(listOf(Indices.DOCUMENTS.indexName))
                .searchType(SearchType.QueryThenFetch)
                .query { queryBuilder ->
                    queryBuilder
                        .bool { boolBuilder ->
                            boolBuilder.must { mustBuilder ->
                                mustBuilder.queryString { queryString ->
                                    queryString.query(query)
                                }
                            }

                        }
                }
        }, String::class.java).documents()
}