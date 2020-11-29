FROM python:3

WORKDIR /app

COPY main.py ./
COPY data/ ./data/

RUN chmod -R g+rwX ./data

USER 1001

CMD ["python3", "main.py"]
