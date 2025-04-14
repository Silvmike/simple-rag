package com.example.demo.entity

import io.hypersistence.utils.hibernate.type.array.FloatArrayType
import jakarta.persistence.*
import org.hibernate.annotations.Type

@Entity
@Table(name = "document_segment")
class DocumentSegment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "vector", columnDefinition = "float[]")
    @Type(FloatArrayType::class)
    val vector: Array<Float>? = null,

    @Column(name = "fragment", nullable = false)
    val fragment: String? = null,

    @JoinColumn(name = "document_id", nullable = false)
    @ManyToOne
    val document: Document? = null,

)