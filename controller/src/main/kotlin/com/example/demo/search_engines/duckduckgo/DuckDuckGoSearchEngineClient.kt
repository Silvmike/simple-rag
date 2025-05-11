package com.example.demo.search_engines.duckduckgo

import com.example.demo.search_engines.api.SearchEngineClient
import com.example.demo.search_engines.api.SearchResult
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.hc.core5.http.message.BasicNameValuePair
import org.apache.hc.core5.net.URIBuilder
import org.jsoup.Jsoup
import java.nio.charset.Charset

class DuckDuckGoSearchEngineClient(
    private val httpClient: OkHttpClient
) : SearchEngineClient {

    private val mapper = ObjectMapper()

    override fun query(query: String): List<SearchResult> =
        searchPage(query).execute().let {
            if (it.code != 200) {
                throw IllegalStateException("Failed to get duckduckgo search page ${it.code} ${it.message}")
            }

            val body = it.body!!.source().readString(Charset.defaultCharset())
            val searchJs = Jsoup.parse(body)
                .head()
                .select("link#deep_preload_link")
                .attr("href")

            httpClient.newCall(
                Request.Builder()
                    .url(searchJs)
                    .pretendToBeABrowser()
                    .get()
                    .build()
            ).execute().let { jsResponse ->
                if (jsResponse.code != 200) {
                    throw IllegalStateException("Failed to get duckduckgo result js ${jsResponse.code} ${jsResponse.message}")
                }
                parseJsResponseBody(jsResponse)
            }
        }

    private fun parseJsResponseBody(jsResponse: Response): List<SearchResult> {
        val responseBody = jsResponse.body!!.source().readString(Charset.defaultCharset())
        val startIndex = responseBody.indexOf(DATA_PREFIX) + DATA_PREFIX.length
        val searchResults = "[{" + responseBody.substring(
            startIndex = startIndex,
            endIndex = responseBody.indexOf(DATA_SUFFIX, startIndex = startIndex)
        ) + "}]"

        val jsonNode = mapper.readTree(searchResults) as ArrayNode
        return jsonNode.asSequence()
            .filter {
                it.has(URL_FIELD_NAME)
            }.map { resultNode ->
                val searchResult =
                    HtmlToTextDocumentTransformer().transform(
                        Document.from(resultNode.get(SEARCH_RESULT_FIELD_NAME).textValue())
                    ).text()
                val url = resultNode.get(URL_FIELD_NAME).textValue()
                SearchResult(
                    url = url,
                    shortDescription = searchResult
                )
            }.toList()
        }

    private fun searchPage(query: String) = httpClient.newCall(
        Request.Builder()
            .url(
                URIBuilder()
                    .setHost("duckduckgo.com")
                    .setScheme("https")
                    .setParameters(
                        BasicNameValuePair("t", "h_"),
                        BasicNameValuePair("q", query),
                        BasicNameValuePair("ia", "web")
                    ).build()
                    .toHttpUrlOrNull()!!
            )
            .pretendToBeABrowser()
            .get()
            .build()
    )

    private fun Request.Builder.pretendToBeABrowser(): Request.Builder =
        this
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")

    companion object {
        private const val DATA_PREFIX = "DDG.pageLayout.load('d',[{"
        private const val DATA_SUFFIX = "}]);"
        private const val URL_FIELD_NAME = "u"
        private const val SEARCH_RESULT_FIELD_NAME = "a"
    }
}