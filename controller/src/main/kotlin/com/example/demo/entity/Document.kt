package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "document")
class Document(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Lob
    @Column(name = "doc", nullable = false)
    val doc: String? = null
)