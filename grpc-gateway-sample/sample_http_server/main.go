/*
 *
 * Copyright 2015 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

// Package main implements a server for Greeter service.
package main

import (
	"context"
	"log"
	"net"
	"net/http"

	pb "example.com/grpc-gateway-sample/gen"
	"github.com/grpc-ecosystem/grpc-gateway/v2/runtime"
)

const (
	port = ":50050"
)

// server is used to implement helloworld.GreeterServer.
type server struct {
	pb.UnimplementedGreeterServer
	pb.UnimplementedEchoServer
}

// SayHello implements helloworld.GreeterServer
func (s *server) SayHello(ctx context.Context, in *pb.HelloRequest) (*pb.HelloReply, error) {
	log.Printf("Received: %v", in.GetName())
	return &pb.HelloReply{Message: "Hello " + in.GetName()}, nil
}

func (s *server) UseOneOf(ctx context.Context, in *pb.OneOfRequest) (*pb.OneOfRootReply, error) {
	log.Printf("Received: %v", in.GetName())
	if in.Name == nil {
		return &pb.OneOfRootReply{Body: &pb.OneOfRootReply_Reply1{&pb.OneOfSub1Reply{Message: "reply1"}}}, nil
	} else {
		return &pb.OneOfRootReply{Body: &pb.OneOfRootReply_Reply2{&pb.OneOfSub2Reply{Message: "reply2"}}}, nil
	}
}

func (s *server) DoEcho(ctx context.Context, in *pb.EchoRequest) (*pb.EchoReply, error) {
	return &pb.EchoReply{Message: in.Message}, nil
}

func main() {
	lis, err := net.Listen("tcp", port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	// ref. https://github.com/hashicorp/boundary/blob/main/internal/servers/controller/handler.go#L49
	ctx := context.Background()
	gw_mux := runtime.NewServeMux()
	pb.RegisterGreeterHandlerServer(ctx, gw_mux, &server{})
	pb.RegisterEchoHandlerServer(ctx, gw_mux, &server{})

	mux := http.NewServeMux()
	mux.Handle("/v1/", gw_mux)

	server := http.Server{
		Handler: mux,
	}
	if server.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
