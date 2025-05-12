package com.example.demo.chat.planning.demo

import com.example.demo.chat.api.MyChat
import com.example.demo.chat.planning.Planner
import com.example.demo.chat.planning.PlanningPromptProviderImpl
import com.example.demo.chat.planning.registry.api.CallableFunctionExecutorRegistry
import com.example.demo.chat.planning.registry.api.CallableFunctionRegistry
import com.example.demo.chat.planning.registry.impl.CallableFunctionAwareExecutor
import com.example.demo.chat.planning.registry.impl.SimpleCallableFunctionExecutorRegistry
import com.example.demo.chat.planning.registry.impl.SimpleCallableFunctionRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Import(FunctionsConfig::class)
@Configuration
class PlannerConfig {

    @Bean
    fun planner(
        myChat: MyChat,
        callableFunctionRegistry: CallableFunctionRegistry,
        callableFunctionExecutorRegistry: CallableFunctionExecutorRegistry
    ) = Planner(
        myChat = myChat,
        callableFunctionExecutorRegistry = callableFunctionExecutorRegistry,
        callableFunctionRegistry = callableFunctionRegistry,
        planningPromptProvider = PlanningPromptProviderImpl()
    )

    @Bean
    fun callableFunctionExecutorRegistry(executors: List<CallableFunctionAwareExecutor>) =
        SimpleCallableFunctionExecutorRegistry().apply {
            executors.forEach { executor ->
                register(executor, executor.callableFunction)
            }
        }

    @Bean
    fun callableFunctionRegistry(executors: List<CallableFunctionAwareExecutor>) =
        SimpleCallableFunctionRegistry().apply {
            executors.forEach { executor ->
                register(executor.callableFunction)
            }
        }
}