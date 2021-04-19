FROM golang:buster

WORKDIR /work

COPY main.go .

RUN apt-get update && \
    apt-get install -y iproute2

CMD ["go", "run", "main.go"]
