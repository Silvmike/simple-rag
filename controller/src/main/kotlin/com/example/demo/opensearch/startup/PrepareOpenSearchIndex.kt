package com.example.demo.opensearch.startup

import com.example.demo.opensearch.Indices
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
    private val client: OpenSearchClient
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun prepare() {

        val mapper: JsonpMapper = client._transport().jsonpMapper()

        Indices.entries.forEach {
            createIndexIfNotExists(mapper, it.indexName)
        }
    }

    private fun createIndexIfNotExists(jsonpMapper: JsonpMapper, name: String) {
        if (hasIndex(name)) {
            logger.info("INDEX [{}] EXISTS, WON'T CREATED!", name)
        }

        val mappings =
            "opensearch/${name}_request_mappings.json".deserialize(jsonpMapper) { parser, mapper ->
                TypeMapping._DESERIALIZER.deserialize(
                    parser, mapper
                )
            }

        val settings =
            "opensearch/${name}_request_settings.json".deserialize(jsonpMapper) { parser, mapper ->
                IndexSettings._DESERIALIZER.deserialize(
                    parser, mapper
                )
            }

        client.indices().create(
            CreateIndexRequest.Builder()
                .index(name)
                .mappings(mappings)
                .settings(settings)
                .build()
        )

        logger.info("INDEX [{}] CREATED SUCCESSFULLY!", name)
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
