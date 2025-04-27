package com.example.demo.util.file

import org.apache.commons.io.monitor.FileAlterationObserver
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class FileSystemWatched<T>(
    protected val path: Path
): DirectoryObserved, PathAlterationListener {

    constructor(resource: Resource): this(
        Paths.get(resource.uri.path),
    )

    private val logger = LoggerFactory.getLogger(javaClass)
    private val lock = ReentrantLock()
    private val observer = PathObserverFactory(path)
    private val cache = AtomicReference<T>()

    abstract val nullValue: T?

    fun get(): T? {
        var result = cache.get()
        if (result == null) {
            lock.withLock {
                result = cache.get()
                if (result == null) {
                    val document = load()
                    cache.set(document)
                    return document
                }
            }
        }
        if (result == nullValue) return null
        return result!!
    }

    abstract fun load(): T

    override fun onFileDelete(file: File?) {
        logger.info("Deleted document from path: [{}]", path)
        cache.set(nullValue)
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