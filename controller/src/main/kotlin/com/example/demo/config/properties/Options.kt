package com.example.demo.config.properties

import com.example.demo.config.properties.options.*
import com.example.demo.config.properties.options.model.ModelsOptions
import com.example.demo.config.properties.options.search_engines.SearchEnginesOptions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties("options")
class Options {

    @NestedConfigurationProperty
    var jdbc: JdbcOptions = JdbcOptions()

    @NestedConfigurationProperty
    var testChat: TestChatOptions = TestChatOptions()

    @NestedConfigurationProperty
    var queryService: QueryServiceOptions = QueryServiceOptions()

    @NestedConfigurationProperty
    var vectorSearch: VectorSearchOptions = VectorSearchOptions()

    @NestedConfigurationProperty
    var fullTextSearch: FullTextSearchOptions = FullTextSearchOptions()

    @NestedConfigurationProperty
    var searchEngine: SearchEnginesOptions = SearchEnginesOptions()

    @NestedConfigurationProperty
    var model: ModelsOptions = ModelsOptions()

}