package com.example.demo.util.template

import dev.langchain4j.data.document.Document

class FallbackTemplateProvider(
    private val main: TemplateProvider,
    private val fallback: TemplateProvider
) : TemplateProvider {

    override fun get(): Document? =
        main.get() ?: fallback.get()
}