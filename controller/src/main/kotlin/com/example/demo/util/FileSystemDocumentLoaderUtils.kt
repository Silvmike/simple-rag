package com.example.demo.util

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import java.nio.file.Paths

fun String.loadResourceDocument(): Document =
    FileSystemDocumentLoader.loadDocument(
        Paths.get(
            javaClass.getResource(this)?.toURI() ?: throw RuntimeException("File not found!")
        )
    )