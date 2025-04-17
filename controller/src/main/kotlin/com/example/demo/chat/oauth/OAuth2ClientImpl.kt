package com.example.demo.chat.oauth

import com.example.demo.chat.oauth.api.OAuth2Client
import com.example.demo.chat.oauth.api.OAuthResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.nio.charset.Charset
import java.util.*


class OAuth2ClientImpl(
    val client: OkHttpClient,
    val scope: String = "GIGACHAT_API_PERS"
) : OAuth2Client {

    private val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    override fun requestToken(authKey: String): OAuthResponse =
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