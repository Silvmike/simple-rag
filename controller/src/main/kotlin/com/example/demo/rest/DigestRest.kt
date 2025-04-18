package com.example.demo.rest

import com.example.demo.service.store.UnsegmentedDocumentService
import com.example.demo.service.transform.HtmlDocumentFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/digest")
class DigestRest(
    private val unsegmentedDocumentService: UnsegmentedDocumentService
) {

    @PostMapping(path = ["/html"], consumes = ["text/html"])
    fun digest(@RequestBody content: String): Long =
        unsegmentedDocumentService.createDocument(
            HtmlDocumentFactory.transform(content)
        )

}