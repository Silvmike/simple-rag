package com.example.demo.dao

import com.example.demo.entity.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentDao : JpaRepository<Document, Long>