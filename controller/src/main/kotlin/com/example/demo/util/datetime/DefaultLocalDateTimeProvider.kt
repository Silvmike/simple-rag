package com.example.demo.util.datetime

object DefaultLocalDateTimeProvider : LocalDateTimeProvider {

    override fun currentTimeMillis(): Long  = System.currentTimeMillis()
}