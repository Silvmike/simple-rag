package com.example.demo.util.file

import org.apache.commons.io.monitor.FileAlterationObserver

interface DirectoryObserved {

    fun getObserver(): FileAlterationObserver

}