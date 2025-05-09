package com.example.demo.chat.planning.registry.api

import com.example.demo.chat.planning.registry.model.CallableFunction

interface CallableFunctionExecutorRegistry {

    fun lookup(function: CallableFunction): CallableFunctionExecutor

}