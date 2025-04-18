package com.example.demo.dao

import com.example.demo.entity.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentDao : JpaRepository<DocumentEntity, Long>