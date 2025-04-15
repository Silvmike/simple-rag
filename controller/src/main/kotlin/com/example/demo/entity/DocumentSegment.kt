package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "document_segment")
class DocumentSegment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "fragment", nullable = false)
    val fragment: String? = null,

    @JoinColumn(name = "document_id", nullable = false)
    @ManyToOne
    val document: Document? = null,

)