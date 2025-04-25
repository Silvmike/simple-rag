package com.example.demo.util.template

import com.example.demo.util.loadResourceDocument
import com.google.common.base.Suppliers
import dev.langchain4j.data.document.Document

class ResourceTemplateProvider(
    resource: String
) : TemplateProvider {

    private val value = Suppliers.memoize {
        resource.loadResourceDocument()
    }

    override fun get(): Document = value.get()
}