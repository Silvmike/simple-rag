package com.example.demo.util.template

import com.example.demo.util.file.DirectoryObserved
import com.example.demo.util.file.PathAlterationListener
import com.example.demo.util.file.PathObserverFactory
import com.example.demo.util.loadResourceDocument
import dev.langchain4j.data.document.Document
import org.apache.commons.io.monitor.FileAlterationObserver
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val NULL_DOCUMENT = Document("null")

class FileSystemWatchedTemplateProvider(
    private val path: Path
) : TemplateProvider, DirectoryObserved, PathAlterationListener {

    constructor(resource: Resource): this(
        Paths.get(resource.uri.path)
    )

    private val logger = LoggerFactory.getLogger(javaClass)

    private val lock = ReentrantLock()
    private val observer = PathObserverFactory(path)
    private val cache = AtomicReference<Document?>()

    override fun get(): Document? {
        var result = cache.get()
        if (result == null) {
            lock.withLock {
                result = cache.get()
                if (result == null) {
                    val document = loadDocument()
                    cache.set(document)
                    return document
                }
            }
        }
        if (result === NULL_DOCUMENT) return null
        return result!!
    }

    private fun loadDocument(): Document =
        try {
            logger.info("Loading document from path: [{}]", path)
            path.loadResourceDocument().apply {
                logger.info("Loaded document from path: [{}]", path)
            }
        } catch (e: Exception) {
            logger.error("Cannot load document [{}]!", path, e)
            NULL_DOCUMENT
        }

    override fun onFileDelete(file: File?) {
        logger.info("Deleted document from path: [{}]", path)
        cache.set(NULL_DOCUMENT)
    }

    override fun onFileCreate(file: File?) {
        logger.info("Created document on path: [{}]", path)
        onFileChange(file)
    }

    override fun onFileChange(file: File?) {
        logger.info("Changed document on path: [{}]", path)
        cache.set(null)
    }

    override fun getObserver(): FileAlterationObserver =
        observer.getObserver(this)
}