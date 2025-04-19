package com.example.demo.rest

import com.example.demo.service.store.UnsegmentedDocumentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/store")
class StoreRest(
    private val unsegmentedDocumentService: UnsegmentedDocumentService
) {

    @DeleteMapping("/document/{id}")
    fun delete(@PathVariable("id") id: Long) {
        unsegmentedDocumentService.deleteDocument(id)
    }

}