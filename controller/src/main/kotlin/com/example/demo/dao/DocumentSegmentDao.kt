package com.example.demo.dao

import com.example.demo.entity.DocumentSegment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DocumentSegmentDao : JpaRepository<DocumentSegment, Long> {

    fun findByVector(vector: Array<Int>): Optional<DocumentSegment>

}