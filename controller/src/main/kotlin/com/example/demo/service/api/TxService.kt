package com.example.demo.service.api

interface TxService {

    fun <T> execute(action: () -> T): T
}