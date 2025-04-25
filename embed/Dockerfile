FROM python:3.12.3

ADD requirements.txt .
RUN pip install -r requirements.txt

# ai-forever/sbert_large_nlu_ru 1024
# intfloat/multilingual-e5-large 1024
# intfloat/multilingual-e5-base 768
# cointegrated/LaBSE-en-ru 768
# DeepPavlov/rubert-base-cased 768
# paraphrase-multilingual-mpnet-base-v2 768
# distiluse-base-multilingual-cased-v2 512

ARG embedding_model=ai-forever/sbert_large_nlu_ru
ENV EMBEDDING_MODEL=$embedding_model

RUN python -c "from sentence_transformers import SentenceTransformer; SentenceTransformer('$embedding_model')"
ADD test.py .

EXPOSE 8000
CMD ["python", "test.py"]
