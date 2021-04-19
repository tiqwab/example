FROM golang:buster

WORKDIR /work

RUN apt-get update && \
    apt-get install -y iproute2 supervisor && \
    go get github.com/path-network/go-mmproxy

COPY main.go .
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

CMD ["/usr/local/bin/docker-entrypoint"]
