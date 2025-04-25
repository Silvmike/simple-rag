package com.example.demo.rest

import com.example.demo.service.store.api.dto.DocumentIdentifier
import com.example.demo.service.store.api.UnsegmentedDocumentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/store")
class StoreRest(
    private val unsegmentedDocumentService: UnsegmentedDocumentService
) {

    @DeleteMapping("/document/id")
    fun delete(@RequestBody id: DocumentIdentifier) {
        unsegmentedDocumentService.deleteDocument(id)
    }

}