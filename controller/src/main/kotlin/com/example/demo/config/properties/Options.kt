package com.example.demo.config.properties

import com.example.demo.config.properties.options.*
import com.example.demo.config.properties.options.model.ModelsOptions
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("options")
class Options {

    var jdbc: JdbcOptions = JdbcOptions()
        @ConfigurationProperties("jdbc")
        set

    var testChat: TestChatOptions = TestChatOptions()
        @ConfigurationProperties("test-chat")
        set

    var queryService: QueryServiceOptions = QueryServiceOptions()
        @ConfigurationProperties("query-service")
        set

    var vectorSearch: VectorSearchOptions = VectorSearchOptions()
        @ConfigurationProperties("vector-search")
        set

    var fullTextSearch: FullTextSearchOptions = FullTextSearchOptions()
        @ConfigurationProperties("full-text-search")
        set

    var model: ModelsOptions = ModelsOptions()
        @ConfigurationProperties("model")
        set

}