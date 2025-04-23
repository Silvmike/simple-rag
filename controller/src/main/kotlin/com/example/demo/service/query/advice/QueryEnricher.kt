package com.example.demo.service.query.advice

interface QueryEnricher {

    fun enrich(query: String): String

}