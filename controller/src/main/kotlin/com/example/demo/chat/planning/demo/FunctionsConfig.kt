package com.example.demo.chat.planning.demo

import com.example.demo.chat.planning.demo.functions.FileReadingFunction
import com.example.demo.chat.planning.demo.functions.MultiFileReadingFunction
import com.example.demo.chat.planning.demo.functions.SearchFunction
import com.example.demo.chat.planning.registry.impl.CallableFunctionAwareExecutor
import com.example.demo.chat.planning.registry.model.CallableArgument
import com.example.demo.chat.planning.registry.model.CallableFunction
import com.example.demo.search_engines.api.SearchEngineClient
import com.fasterxml.jackson.databind.JsonNode
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
            responseModel = TextNode("Результаты поиска в текстовом виде"),
            cost = 130.0f
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
                      { "system_id": "CPC", "aliases": ["CPC", "КПК"] },
                      { "system_id": "BIS", "aliases": ["БИС"] }
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
            arguments = listOf(
                CallableArgument(
                    name = "system_id", description = "System identifier obtained from get_systems"
                )
            ),
            description = "Позволяет получить исчерпывающий список сервисов, предоставляемых системой-источником",
            responseModel = mapper.readTree(
                """
                    [
                      {
                        "service_id": "cpc_get_data",
                        "description": "Позволяет получить информацию по продуктам клиента"
                      }
                    ]
                """.trimIndent()
            ),
            cost = 1.1f
        ),
        executor = MultiFileReadingFunction(
            argumentNames = listOf("system_id"),
            sourceFileNameFormatter = { parsedArgs ->
                "/docs/${parsedArgs["system_id"]}_services.json"
            }
        ) { function, arguments, exception ->
            val argument: JsonNode? = arguments["system_id"]
            if (argument == null) {
                "Missing 'system_id' argument"
            } else {
                "No such system with system_id: '$argument'. Please use only system_id obtained from get_systems!"
            }
        }
    )

    @Bean
    fun getSystemModel(): Any = CallableFunctionAwareExecutor(
        callableFunction = CallableFunction(
            name = "get_model",
            arguments = listOf(
                CallableArgument(
                    name = "service_id", description = "Service identifier obtained from get_system_services"
                ),
                CallableArgument(
                    name = "model_id", description = "Model identifier"
                )
            ),
            description = "Позволяет получить описание указанной модели данных для указанного сервиса",
            responseModel = mapper.readTree(
                """
                    {
                        "attr1": "description1",
                        "attr2": "description2"
                    }
                """.trimIndent()
            ),
            cost = 1.1f
        ),
        executor = MultiFileReadingFunction(
            argumentNames = listOf("service_id", "model_id"),
            sourceFileNameFormatter = { parsedArgs ->
                "/docs/models/${parsedArgs["service_id"]}/${parsedArgs["model_id"]}.json"
            }
        ) { function, arguments, exception ->
            val serviceId: JsonNode? = arguments["service_id"]
            val modelId: JsonNode? = arguments["model_id"]
            if (serviceId == null) {
                "Missing 'service_id' argument"
            } else if (modelId == null) {
                "Missing 'model_id' argument"
            } else {
                "Model not found for model_id: '$modelId' for service with service_id: '$serviceId'. " +
                "Please use only service_id obtained from get_system_services!"
            }
        }
    )

}