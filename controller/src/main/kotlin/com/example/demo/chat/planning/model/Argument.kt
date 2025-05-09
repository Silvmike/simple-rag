package com.example.demo.chat.planning.model

import com.fasterxml.jackson.databind.JsonNode

data class Argument(
    val name: String,
    val value: JsonNode?
)
