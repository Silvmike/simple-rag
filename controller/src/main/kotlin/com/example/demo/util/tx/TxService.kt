package com.example.demo.util.tx

interface TxService {

    fun <T> execute(action: () -> T): T
}