package main

import (
	"fmt"
	"net"
	"os"
)

func main() {

	listener, err := net.Listen("tcp", "0.0.0.0:6379")
	if err != nil {
		fmt.Println("Failed to bind to port 6379")
		os.Exit(1)
	}
	defer listener.Close()
	fmt.Println("Listening on port")

	for {
		connection, err := listener.Accept()
		if err != nil {
			fmt.Println("Error accepting connection: ", err.Error())
			continue
		}
		fmt.Println("New connection")
		go handleConnection(connection)
	}
}

func handleConnection(connection net.Conn) {
	defer connection.Close()
	for {
		buffer := make([]byte, 1024)
		n, err := connection.Read(buffer)
		if err != nil {
			fmt.Println("Error reading from connection: ", err.Error())
			return
		}
		fmt.Println("Received: ", string(buffer[0:n]))
		message := []byte("+PONG\r\n")
		connection.Write(message)
	}
}
