package com.example.demo.util.file

import org.apache.commons.io.IOCase
import org.apache.commons.io.monitor.FileAlterationObserver
import org.apache.commons.io.monitor.FileEntry
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.name

class PathObserverFactory(
    private val path: Path
) {

    init {
        if (path.isDirectory()) throw IllegalArgumentException("Path is a directory")
    }

    fun getObserver(listener: PathAlterationListener): FileAlterationObserver =
        FileAlterationObserver.builder()
            .setFileFilter { it.name == path.name }
            .setIOCase(IOCase.SYSTEM)
            .setRootEntry(FileEntry(path.parent.toFile()))
            .get().apply {
                addListener(listener)
            }
}