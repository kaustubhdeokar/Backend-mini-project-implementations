package main

import "fmt"

type List[T any] struct {
	items []T
}

func (list *List[T]) Add(item T) {
	list.items = append(list.items, item)
}

func (list *List[T]) removeLast() {
	list.items = list.items[:len(list.items)-1]
}

func (list *List[T]) removeElemAtIndex(index int) {
	list.items[index] = list.items[len(list.items)-1]
	list.items = append(list.items[:index], list.items[index+1:]...)
}

func (list List[T]) printAll() {
	for _, item := range list.items {
		fmt.Print(item, " ")
	}
	fmt.Println()
}

func executeList(){
	var intList List[int]

	// Add integers to the list
	intList.Add(10)
	intList.Add(20)
	intList.Add(30)
	intList.Add(10)
	intList.Add(20)
	intList.Add(30)

	intList.printAll()

	intList.removeElemAtIndex(1)
	intList.printAll()
}