# the compiler will read input files in `proto/` and write output to `gen/`
# see https://developers.google.com/protocol-buffers/docs/reference/go-generated for paths
proto:
	protoc --proto_path=proto --go_out=gen --go_opt=paths=source_relative addressbook.proto sub/person.proto

run: proto
	go run main.go

.PHONY: proto run
