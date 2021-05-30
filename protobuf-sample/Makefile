proto:
	protoc -I=proto --go_out=gen proto/*.proto

run: proto
	go run main.go

.PHONY: proto run
