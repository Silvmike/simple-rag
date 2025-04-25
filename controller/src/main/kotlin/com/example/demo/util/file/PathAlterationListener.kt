package com.example.demo.util.file

import org.apache.commons.io.monitor.FileAlterationListener
import org.apache.commons.io.monitor.FileAlterationObserver
import java.io.File

interface PathAlterationListener : FileAlterationListener {

    override fun onDirectoryChange(directory: File?) {}

    override fun onDirectoryDelete(directory: File?) {}

    override fun onDirectoryCreate(directory: File?) {}

    override fun onFileChange(file: File?) {}

    override fun onFileCreate(file: File?) {}

    override fun onFileDelete(file: File?) {}

    override fun onStart(observer: FileAlterationObserver?) {}

    override fun onStop(observer: FileAlterationObserver?) {}
}