package main

import "fmt"

func describeValue() interface{} {

	// mysterBox := interface{}(10)
	mysterBox := interface{}("helloadasdasdasd")
	return mysterBox

}

func Typecheckhelper() {

	mysterBox := describeValue()

	value, ok := mysterBox.(int)
	if ok {
		fmt.Println("int value:", value)
	} else {
		fmt.Println("not int :", value)
	}

}
