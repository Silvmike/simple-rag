from typing import List
from typing import Optional

import torch
import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel
from transformers import AutoModelForSequenceClassification, AutoTokenizer
import os

model_name = os.getenv("RERANKER_MODEL")

tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name).eval().float()

class Document(BaseModel):
    document: str
    score: float

def rerank_docs(query: str, docs: list[str], top_k: int = 10) -> list[Document]:
    """
    Ранжирует документы по релевантности запросу.
    Возвращает список кортежей (документ, оценка).
    """
    try:
        # Токенизация пар запрос-документ
        pairs = [[query, doc] for doc in docs]
        inputs = tokenizer(
            pairs,
            padding=True,
            truncation=True,
            return_tensors="pt",
            max_length=512
        ).to("cpu")

        # Предсказание релевантности
        with torch.no_grad():
            scores = model(**inputs).logits.squeeze(-1).cpu().numpy()

        # Сортировка по убыванию релевантности
        ranked_results = sorted(zip(docs, scores), key=lambda x: x[1], reverse=True)
        return list(map(
            lambda item: Document(document=item[0], score=item[1]),
            ranked_results[:top_k]
        ))

    except Exception as e:
        print(f"Ошибка: {e}")
        return []

app = FastAPI()

class RerankRequest(BaseModel):
    query: str
    docs: List[str]
    top_k: Optional[int] = None

class RerankResponse(BaseModel):
    reranked_docs: List[Document]

@app.post("/rerank")
async def rerank(request: RerankRequest) -> RerankResponse:
    return RerankResponse(
        reranked_docs = rerank_docs(
            request.query,
            request.docs,
            request.top_k if request.top_k is not None else 2
        )
    )

@app.get("/health",)
def get_health():
    return "OK"

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)