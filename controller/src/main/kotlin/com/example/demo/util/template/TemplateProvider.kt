package com.example.demo.util.template

import dev.langchain4j.data.document.Document
import java.util.function.Supplier

interface TemplateProvider : Supplier<Document?>