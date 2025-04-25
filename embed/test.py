from fastapi import FastAPI
from typing import List
from sentence_transformers import SentenceTransformer
import uvicorn
import os

app = FastAPI()
model = SentenceTransformer(os.getenv("EMBEDDING_MODEL"), device='cpu')

@app.post("/embed")
async def embed(texts: List[str]):
    return model.encode(texts).tolist()

@app.get("/health",)
def get_health():
    return "OK"

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
