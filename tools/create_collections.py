#!/usr/bin/env python

from qdrant_client import QdrantClient, models

client = QdrantClient(url="http://localhost:6333")

col_name="embeddings"

client.create_collection(
    collection_name=col_name,
    vectors_config=models.VectorParams(size=1024, distance=models.Distance.COSINE)
)
