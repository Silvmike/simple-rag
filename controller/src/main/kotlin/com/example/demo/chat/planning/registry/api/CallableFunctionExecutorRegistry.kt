package com.example.demo.chat.planning.registry.api

import com.example.demo.chat.planning.registry.model.CallableFunction
import java.util.*

interface CallableFunctionExecutorRegistry {

    fun lookup(function: CallableFunction): Optional<CallableFunctionExecutor>

}