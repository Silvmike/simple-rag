package com.example.demo.rest

import com.example.demo.service.store.api.dto.CreateDocumentResponse
import com.example.demo.service.store.api.UnsegmentedDocumentService
import com.example.demo.util.transform.HtmlDocumentFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/digest")
class DigestRest(
    private val unsegmentedDocumentService: UnsegmentedDocumentService
) {

    @PostMapping(path = ["/html"], consumes = ["text/html"])
    fun digest(@RequestBody content: String): CreateDocumentResponse =
        unsegmentedDocumentService.createDocument(
            HtmlDocumentFactory.transform(content)
        )

}