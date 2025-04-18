package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "document")
class DocumentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Lob
    @Column(name = "doc", nullable = false)
    var content: String? = null
)