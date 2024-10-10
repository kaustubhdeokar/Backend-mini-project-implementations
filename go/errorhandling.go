package main

import (
	"fmt"
	"math"
)

func errorHelper() {

	res, error := calculateSqrt(1)
	printHelper(res, error)

	res, error = calculateSqrt(-1)
	printHelper(res, error)
}

func printHelper(res float64, error error) {
	if error != nil {
		fmt.Println(error.Error())
	} else {
		fmt.Println(res)
	}
}

type CalculationError struct {
	msg string
}

func (ce CalculationError) Error() string {
	return ce.msg
}

func calculateSqrt(num float64) (float64, error) {
	if num < 0 {
		return 0, CalculationError{msg: "Error: No negative allowed"}
	}
	return math.Sqrt(num), nil
}
