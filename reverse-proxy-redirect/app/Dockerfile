FROM golang:1-buster AS builder

WORKDIR /work
COPY server.go server.go
RUN go build server.go


FROM ubuntu:20.04

WORKDIR /work
COPY --from=builder /work/server ./server
CMD ["./server"]
