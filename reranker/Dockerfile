FROM python:3.12.3

ADD requirements.txt .
RUN pip install -r requirements.txt

# distilbert-base-multilingual-cased
# BAAI/bge-reranker-large
# cross-encoder/ms-marco-MiniLM-L-6-v2
# ce-esim-manxcon/msmarco-reranker-xlm-r-paraphrase
# intfloat/simlm-msmarco-reranker

ARG reranker_model=BAAI/bge-reranker-large
ENV RERANKER_MODEL=$reranker_model

RUN python -c "from transformers import AutoModelForSequenceClassification; AutoModelForSequenceClassification.from_pretrained('$reranker_model')"
ADD reranker.py .

EXPOSE 8001
CMD ["python", "reranker.py"]
