package com.example.demo.chat.planning.demo

import com.example.demo.chat.planning.demo.functions.FileReadingFunction
import com.example.demo.chat.planning.demo.functions.MultiFileReadingFunction
import com.example.demo.chat.planning.demo.functions.SearchFunction
import com.example.demo.chat.planning.registry.impl.CallableFunctionAwareExecutor
import com.example.demo.chat.planning.registry.model.CallableArgument
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.search_engines.api.SearchEngineClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FunctionsConfig {

    private val mapper = ObjectMapper()

    @Bean
    fun searchFunction(
        searchEngine: SearchEngineClient
    ) = CallableFunctionAwareExecutor(
        callableFunction = CallableFunction(
            name = "search",
            arguments = listOf(
                CallableArgument(name = "query", description = "Поисковой запрос")
            ),
            description = "Выполняет поиск информации в поисковой системе",
            responseModel = TextNode("Результаты поиска в текстовом виде")
        ),
        executor = SearchFunction(searchEngine)
    )

    @Bean
    fun getSystems() = CallableFunctionAwareExecutor(
        callableFunction = CallableFunction(
            name = "get_systems",
            arguments = listOf(),
            description = "Позволяет получить список систем",
            responseModel = mapper.readTree(
                """
                    [
                      { "name": "CPC", "aliases": ["CPC", "КПК"] },
                      { "name": "BIS", "aliases": ["БИС"] }
                    ]
                """.trimIndent()
            )
        ),
        executor = FileReadingFunction(
            ClassPathResource("/docs/systems.json")
        )
    )

    @Bean
    fun getStands() = CallableFunctionAwareExecutor(
        callableFunction = CallableFunction(
            name = "get_stands",
            arguments = listOf(),
            description = "Позволяет получить список существующих типов окружения",
            responseModel = mapper.readTree(
                """
                    [
                      { "name": "ИФТ", "description": "Стенд функционального тестирования" },
                      { "name": "НТ", "description": "Стенд нагрузочного тестирования" }
                    ]
                """.trimIndent()
            )
        ),
        executor = FileReadingFunction(
            ClassPathResource("/docs/stands.json")
        )
    )

    @Bean
    fun getSystemServices() = CallableFunctionAwareExecutor(
        callableFunction = CallableFunction(
            name = "get_system_services",
            arguments = listOf(),
            description = "Позволяет получить информацию о сервисах, предоставляемых системой-источником",
            responseModel = mapper.readTree(
                """
                    [
                      {
                        "service_id": "cpc_get_data",
                        "description": "Позволяет получить информацию по продуктам клиента"
                      }
                    ]
                """.trimIndent()
            )
        ),
        executor = MultiFileReadingFunction(
            resourcePrefix = "/docs/",
            argumentName = "system",
            resourceSuffix = "_services.json"
        )
    )

}