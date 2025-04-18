package com.example.demo.service.segmentation

import com.example.demo.service.api.DomainDocument
import com.example.demo.service.api.SegmentedDocument

interface Segmenter {

    fun split(document: DomainDocument): SegmentedDocument

}