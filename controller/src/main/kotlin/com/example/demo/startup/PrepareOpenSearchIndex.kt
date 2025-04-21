package com.example.demo.startup

import jakarta.annotation.PostConstruct
import jakarta.json.stream.JsonParser
import org.opensearch.client.json.JsonpMapper
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.mapping.TypeMapping
import org.opensearch.client.opensearch.indices.CreateIndexRequest
import org.opensearch.client.opensearch.indices.IndexSettings
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import java.io.InputStreamReader
import java.util.function.BiFunction


class PrepareOpenSearchIndex(
    val client: OpenSearchClient
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun prepare() {

        val mapper: JsonpMapper = client._transport().jsonpMapper()

        if (hasIndex("myindex")) {
            logger.info("INDEX EXISTS, WON'T CREATED!")
        }

        val mappings =
            "opensearch/myindex_request_mappings.json".deserialize(mapper) { parser, mapper ->
                TypeMapping._DESERIALIZER.deserialize(
                    parser, mapper
                )
            }

        val settings =
            "opensearch/myindex_request_settings.json".deserialize(mapper) { parser, mapper ->
                IndexSettings._DESERIALIZER.deserialize(
                    parser, mapper
                )
            }

        client.indices().create(
            CreateIndexRequest.Builder()
                .index("myindex")
                .mappings(mappings)
                .settings(settings)
                .build()
        )

        logger.info("INDEX CREATED SUCCESSFULLY!")
    }

    private fun hasIndex(indexName: String) = client.cat().indices().valueBody().any {
        it.index() == indexName
    }

    private fun <T> String.deserialize(mapper: JsonpMapper, action: BiFunction<JsonParser, JsonpMapper, T>): T =
        InputStreamReader(
            ClassPathResource(this).inputStream
        ).use { mappingsReader ->
            mapper
                .jsonProvider()
                .createParser(mappingsReader).use { parser ->
                    action.apply(parser, mapper)
                }
        }
}
