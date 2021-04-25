package main

import (
	"io"
	"log"
	"net"
	"os"
)

func handleConnection(conn *net.TCPConn) {
	defer conn.Close()

	buf := make([]byte, 4*1024)

	for {
		n, err := conn.Read(buf)
		if err != nil {
			if ne, ok := err.(net.Error); ok {
				switch {
				case ne.Temporary():
					continue
				}
			}
			if err == io.EOF {
				return
			}
			log.Printf("Read: %#v\n", err)
			return
		}

		n, err = conn.Write(buf[:n])
		if err != nil {
			log.Printf("Write: %#v\n", err)
			return
		}
	}
}

func handleListener(l *net.TCPListener) error {
	defer l.Close()
	for {
		conn, err := l.AcceptTCP()
		if err != nil {
			if ne, ok := err.(net.Error); ok {
				if ne.Temporary() {
					log.Printf("AcceptTCP: %#v\n", err)
					continue
				}
			}
			return err
		}

		log.Printf("client: %v\n", conn.RemoteAddr())
		go handleConnection(conn)
	}
}

func doMain() int {
	tcpAddr, err := net.ResolveTCPAddr("tcp", "0.0.0.0:18080")
	if err != nil {
		log.Printf("ResolveTCPAddr: %#v\n", err)
		return 1
	}

	l, err := net.ListenTCP("tcp", tcpAddr)
	if err != nil {
		log.Printf("ListenTCP: %#v\n", err)
		return 1
	}

	err = handleListener(l)
	if err != nil {
		log.Printf("handleListener: %#v\n", err)
		return 1
	}

	return 0
}

func main() {
	os.Exit(doMain())
}
