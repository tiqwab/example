Sample code for [grpc-gateway](https://github.com/grpc-ecosystem/grpc-gateway).

Original code is: https://github.com/grpc/grpc-go/tree/master/examples/helloworld

### How to use

```
# To generate codes
$ buf generate

# Run server
$ go run sample_server/main.go

# Run gateway
$ go run sample_gateway/main.go

# Run client
$ go run sample_client/main.go

# Send request by HTTP
$ curl -H "Content-Type: application/json" http://localhost:50050/v1/hello -d '{"name": "Alice"}'
```

`sample_http_server` is a server which handle HTTP requests directly, so it doesn't need gateway.

```
$ go run sample_http_server/main.go

$ curl -H "Content-Type: application/json" http://localhost:50050/v1/hello -d '{"name": "Alice"}'
```

`buf generate` also generates OpenAPI specs.
We can use `swagger mixin` if we want to merge the generated specs.

```
# Use go-swagger https://github.com/go-swagger/go-swagger
$ find ./gen -name '*.swagger.json' | xargs swagger mixin > controller.swagger.json
```

### Directory structure

- `helloworld/`
  - our protobufs
- `third-party/`
  - protobufs from third-party
- `gen/`
  - generated codes
