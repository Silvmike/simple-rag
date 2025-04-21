package com.example.demo.chat

import com.example.demo.util.UnsafeTls
import okhttp3.OkHttpClient
import javax.net.ssl.X509TrustManager

object UnsafeClientFactory {

    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .sslSocketFactory(
                UnsafeTls.sslContext.socketFactory,
                UnsafeTls.trustAllCerts[0] as X509TrustManager)
            .build()
    }

}