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

// Package main implements a client for Greeter service.
package main

import (
	"context"
	"log"
	"os"
	"time"

	pb "example.com/grpc-gateway-sample/gen"
	"google.golang.org/grpc"
)

const (
	address     = "localhost:50051"
	defaultName = "world"
)

func main() {
	// Set up a connection to the server.
	conn, err := grpc.Dial(address, grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()

	//
	// greeter
	//

	greeter_cli := pb.NewGreeterClient(conn)

	// Contact the server and print out its response.
	name := defaultName
	if len(os.Args) > 1 {
		name = os.Args[1]
	}
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	{
		r, err := greeter_cli.SayHello(ctx, &pb.HelloRequest{Name: name})
		if err != nil {
			log.Fatalf("could not greet: %v", err)
		}
		log.Printf("Greeting: %s", r.GetMessage())
	}
	{
		for _, s := range []*string{&name, nil} {
			r, err := greeter_cli.UseOneOf(ctx, &pb.OneOfRequest{Name: s})
			if err != nil {
				log.Fatalf("could not greet: %v", err)
			}
			switch body := r.Body.(type) {
			case *pb.OneOfRootReply_Reply1:
				log.Printf("nil requested: %T", body)
			case *pb.OneOfRootReply_Reply2:
				log.Printf("%s requested: %T", *s, body)
			default:
				log.Fatalf("unknown body")
			}
		}
	}

	//
	// echo
	//

	echo_cli := pb.NewEchoClient(conn)
	{
		r, err := echo_cli.DoEcho(ctx, &pb.EchoRequest{Message: "do echo"})
		if err != nil {
			log.Fatalf("could not echo: %v", err)
		}
		log.Printf("Echo: %s", r.GetMessage())
	}
}
