package com.example.demo.chat.oauth

import com.example.demo.chat.UnsafeClientFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset
import java.util.*


class OAuth2Client(
    val client: OkHttpClient,
    val scope: String = "GIGACHAT_API_PERS"
) {

    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    fun requestToken(
        clientId: String,
        clientSecret: String,
    ): OAuthResponse = requestToken(
        Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())
    )

    fun requestToken(authKey: String): OAuthResponse =
        mapper.readValue(
            client.newCall(
                Request.Builder()
                    .url("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                    .header("Accept", "application/json")
                    .header("RqUID", UUID.randomUUID().toString())
                    .header("Authorization", "Basic $authKey")
                    .post(
                        FormBody.Builder()
                            .add("scope", scope)
                            .build()
                    )
                    .build()
            ).execute().let {
                if (it.code == 200) {
                    it.body!!.source().readString(Charset.defaultCharset())
                } else {
                    throw RuntimeException(
                        it.body!!.source().readString(Charset.defaultCharset())
                    )
                }
            },
            OAuthResponse::class.java
        )

}

fun main() {
    val oAuth2Client = OAuth2Client(UnsafeClientFactory.create())
    println(oAuth2Client.requestToken("aaaaa"))
}