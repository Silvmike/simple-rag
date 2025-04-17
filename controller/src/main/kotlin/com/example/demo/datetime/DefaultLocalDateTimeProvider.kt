package com.example.demo.datetime

object DefaultLocalDateTimeProvider : LocalDateTimeProvider {

    override fun currentTimeMillis(): Long  = System.currentTimeMillis()
}