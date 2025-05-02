FROM ollama/ollama:0.6.6

ENV model_name="ilyagusev/saiga_llama3"

EXPOSE 11434

RUN apt-get update
RUN apt-get install curl -y

COPY wait-for-llama-start.sh .
RUN chmod +x ./wait-for-llama-start.sh
RUN ollama serve & ./wait-for-llama-start.sh; ollama pull $model_name; ps -ef | grep 'ollama serve' | grep -v grep | awk '{print $2}' | xargs -r kill -9

ENTRYPOINT ["/bin/ollama"]
CMD ["serve"]