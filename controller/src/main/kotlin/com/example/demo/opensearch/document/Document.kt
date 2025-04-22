package com.example.demo.opensearch.document

import com.fasterxml.jackson.annotation.JsonProperty

data class Document(
    @JsonProperty("document_id")
    val documentId: String,
    @JsonProperty("content")
    val content: String
)
