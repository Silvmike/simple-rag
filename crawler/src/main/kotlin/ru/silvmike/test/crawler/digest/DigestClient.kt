package ru.silvmike.test.crawler.digest

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class DigestClient(
    private val client: OkHttpClient
) {

    fun digest(document: String) {
        client.newCall(
            Request.Builder()
                .url("http://localhost:8080/digest/html")
                .post(
                    document.toRequestBody("text/html".toMediaType())
                )
                .build()
        ).execute().apply {
            println("Posted document with response code $code and body: ${body!!.string()}")
        }
    }

}