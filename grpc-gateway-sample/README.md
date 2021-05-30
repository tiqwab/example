Sample code for [grpc-gateway](https://github.com/grpc-ecosystem/grpc-gateway).

Original code is: https://github.com/grpc/grpc-go/tree/master/examples/helloworld

### How to use

```
# To generate codes
$ buf generate

# Run server
$ go run greeter_server/main.go

# Run gateway
$ go run greeter_gateway/main.go

# Run client
$ go run greeter_client/main.go

# Send request by HTTP
$ curl -H "Content-Type: application/json" http://localhost:50050/v1/hello -d '{"name": "Alice"}'
```

### Directory structure

- `helloworld/`
  - our protobufs
- `third-party/`
  - protobufs from third-party
- `gen/`
  - generated codes
