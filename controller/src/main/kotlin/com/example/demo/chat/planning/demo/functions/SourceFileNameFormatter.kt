package com.example.demo.chat.planning.demo.functions

fun interface SourceFileNameFormatter {

    fun format(arguments: Map<String, String?>): String

}