package main

import (
	"fmt"
	"sync"
)

func count(c chan []int, wg *sync.WaitGroup) {
	defer wg.Done()
	val, ok := <-c
	if !ok {
		fmt.Println("closed")
		return
	}
	for i := range val {
		println(i)
	}
}

func addToSlice(c chan []int, i int, wg *sync.WaitGroup) {
	defer wg.Done()
	var slice []int
	for n := 0; n < i; n++ {
		slice = append(slice, n)
	}
	c <- slice
}

func worker(done chan bool) {
	done <- true
}

func invokeCount() {
	var wg sync.WaitGroup
	wg.Add(2)
	channel := make(chan []int)

	go addToSlice(channel, 10, &wg)
	go count(channel, &wg)

	wg.Wait()

	done := make(chan bool, 1)
	go worker(done)
	val, ok := <-done
	fmt.Print(val, ok)

}
