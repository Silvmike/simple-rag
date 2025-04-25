package com.example.demo.util.file

import org.apache.commons.io.monitor.FileAlterationMonitor
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean

class SelfStartedFileAlterationMonitor(
    private val observed: List<DirectoryObserved>
) : InitializingBean, DisposableBean {

    private val monitor = FileAlterationMonitor().apply {
        setThreadFactory { runnable ->
            val thread = Thread(runnable)
            thread.isDaemon = true
            thread
        }
        observed.forEach { observed ->
            addObserver(observed.getObserver())
        }
    }

    override fun afterPropertiesSet() {
        monitor.start()
    }

    override fun destroy() {
        monitor.stop()
    }

}