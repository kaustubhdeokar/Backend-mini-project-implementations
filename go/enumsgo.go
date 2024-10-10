package main

import "fmt"

type ServerState int

const (
	StateIdle      ServerState = 1
	StateConnected ServerState = 2
	StateError     ServerState = 3
	StateRetrying  ServerState = 4
)

func executeEnums() {
	var state ServerState = StateRetrying
	fmt.Println(state)
}
