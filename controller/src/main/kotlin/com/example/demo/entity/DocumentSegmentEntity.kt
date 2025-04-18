package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "document_segment")
class DocumentSegmentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "fragment", nullable = false)
    var fragment: String? = null,

    @Column(name = "start_pos", nullable = false)
    var startPos: Int? = null,

    @Column(name = "end_pos", nullable = false)
    var endPos: Int? = null,

    @Column(name = "qdrant_id", nullable = false)
    var qdrandtId: String? = null,

    @JoinColumn(name = "document_id", nullable = false)
    @ManyToOne
    var document: DocumentEntity? = null
)