package com.example.demo.service

import com.example.demo.service.api.TxService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TxServiceImpl : TxService {

    @Transactional
    override fun <T> execute(action: () -> T): T = action()
}