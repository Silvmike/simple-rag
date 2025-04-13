FROM python:3.12.3

ADD requirements.txt .
RUN pip install -r requirements.txt
RUN python -c "from sentence_transformers import SentenceTransformer; SentenceTransformer('ai-forever/sbert_large_nlu_ru')"
ADD test.py .

EXPOSE 8000
CMD ["python", "test.py"]
