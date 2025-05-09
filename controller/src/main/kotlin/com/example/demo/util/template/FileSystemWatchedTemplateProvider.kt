package com.example.demo.util.template

import com.example.demo.util.file.DirectoryObserved
import com.example.demo.util.file.FileSystemWatched
import com.example.demo.util.file.PathAlterationListener
import com.example.demo.util.loadResourceDocument
import dev.langchain4j.data.document.Document
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import java.nio.file.Path
import java.nio.file.Paths

private val NULL_DOCUMENT = Document("null")

class FileSystemWatchedTemplateProvider(
    path: Path
) : FileSystemWatched<Document?>(path), TemplateProvider {

    constructor(resource: Resource): this(
        Paths.get(resource.uri.path)
    )

    private val logger = LoggerFactory.getLogger(javaClass)

    override val nullValue = NULL_DOCUMENT

    override fun load(): Document =
        try {
            logger.info("Loading document from path: [{}]", path)
            path.loadResourceDocument().apply {
                logger.info("Loaded document from path: [{}]", path)
            }
        } catch (e: Exception) {
            logger.error("Cannot load document [{}]!", path, e)
            NULL_DOCUMENT
        }
}