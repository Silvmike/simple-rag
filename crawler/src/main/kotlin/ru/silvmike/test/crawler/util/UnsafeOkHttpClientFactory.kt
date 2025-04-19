package ru.silvmike.test.crawler.util

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.Duration
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object UnsafeOkHttpClientFactory {

    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofMinutes(2))
            .sslSocketFactory(
                UnsafeClientFactory.sslContext.socketFactory,
                UnsafeClientFactory.trustAllCerts[0] as X509TrustManager
            )
            .build()
    }

}