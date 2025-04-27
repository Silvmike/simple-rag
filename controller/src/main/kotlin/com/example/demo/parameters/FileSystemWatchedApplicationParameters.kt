package com.example.demo.parameters

import com.example.demo.util.file.FileSystemWatched
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class FileSystemWatchedApplicationParameters(
    path: Path,
    private val defaultValues: SimpleApplicationParameters
) : FileSystemWatched<ApplicationParameters?>(path), ApplicationParameters {

    constructor(resource: Resource, defaultValues: SimpleApplicationParameters) : this(
        Paths.get(resource.uri.path),
        defaultValues
    )

    private val logger = LoggerFactory.getLogger(javaClass)

    override val fullTextSearchMaxResults: Int
        get() = params().fullTextSearchMaxResults

    override val vectorSearchMaxResults: Int
        get() = params().vectorSearchMaxResults

    override val rerankerMaxResults: Int
        get() = params().rerankerMaxResults

    private fun params() = (get() ?: defaultValues)

    override val nullValue = defaultValues

    override fun load(): ApplicationParameters =
        try {
            logger.info("Loading parameters from path: [{}]", path)
            val properties = Properties()
            properties.load(path.toFile().inputStream())

            SimpleApplicationParameters(
                fullTextSearchMaxResults = properties.getProperty("search.full_text.max_results").toInt(),
                vectorSearchMaxResults = properties.getProperty("search.vector.max_results").toInt(),
                rerankerMaxResults = properties.getProperty("search.rerank.max_results").toInt()
            ).apply {
                logger.info("Loaded parameters from path: [{}]", path)
            }
        } catch (e: Exception) {
            logger.error("Could not load parameters from path: [{}]", path, e)
            defaultValues
        }
}