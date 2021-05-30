package main

import (
	pb "example.com/protobuf-sample/gen"
	sub "example.com/protobuf-sample/gen/sub"
	"fmt"
	"github.com/golang/protobuf/proto"
	"io/ioutil"
	"os"
)

func simple_write(fileName string) error {
	email := "jdoe@example.com"

	pp := []*sub.Person{
		&sub.Person{
			Id:    1234,
			Name:  "John Doe",
			Email: &email,
			Phones: []*sub.Person_PhoneNumber{
				{Number: "554-4321", Type: sub.Person_HOME},
			},
		},
		&sub.Person{
			Id:    1234,
			Name:  "John Doe",
			Email: nil,
			Phones: []*sub.Person_PhoneNumber{
				{Number: "554-4321", Type: sub.Person_HOME},
			},
		},
	}

	book := &pb.AddressBook{}
	book.People = append(book.People, pp...)

	out, err := proto.Marshal(book)
	if err != nil {
		return err
	}

	if err := ioutil.WriteFile(fileName, out, 0644); err != nil {
		return err
	}

	return nil
}

func simple_read(fileName string) (*pb.AddressBook, error) {
	buf, err := ioutil.ReadFile(fileName)
	if err != nil {
		return nil, err
	}

	var book pb.AddressBook
	if err := proto.Unmarshal(buf, &book); err != nil {
		return nil, err
	}

	return &book, nil
}

func main() {
	const fileName = "addressbook.data"

	if err := simple_write(fileName); err != nil {
		fmt.Printf("%v\n", err)
		os.Exit(1)
	}

	book, err := simple_read(fileName)
	if err != nil {
		fmt.Printf("%v\n", err)
		os.Exit(1)
	}
	fmt.Printf("%v\n", book)
}
