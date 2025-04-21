package com.example.demo.opensearch

import com.example.demo.util.UnsafeTls
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder
import org.apache.hc.core5.http.HttpHost
import org.apache.hc.core5.http.nio.ssl.TlsStrategy
import org.opensearch.client.transport.OpenSearchTransport
import org.opensearch.client.transport.httpclient5.ApacheHttpClient5TransportBuilder

object UnsafeOpenSearchClientFactory {

    fun create(host: HttpHost): OpenSearchTransport =
        with(ApacheHttpClient5TransportBuilder.builder(host)) {
            setHttpClientConfigCallback { httpClientBuilder ->

                val tlsStrategy: TlsStrategy =
                    ClientTlsStrategyBuilder.create()
                        .setSslContext(UnsafeTls.sslContext)
                        .build()
                val connectionManager =
                    PoolingAsyncClientConnectionManagerBuilder.create()
                        .setTlsStrategy(tlsStrategy)
                        .build()

                httpClientBuilder.setConnectionManager(connectionManager)
            }
            build()
        }

}