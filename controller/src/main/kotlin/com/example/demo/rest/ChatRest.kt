package com.example.demo.rest

import com.example.demo.chat.api.MyChat
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/chat")
class ChatRest(
    private val myChat: MyChat
) {

    @PostMapping(path = ["/completions"], consumes = ["text/plain"], produces = ["text/plain"])
    fun getCompletions(@RequestBody query: String): String = myChat.exchange(query)

}