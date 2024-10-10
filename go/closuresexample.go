package main

import "fmt"

func closureHelper() {

	gc1 := giftCard()
	ans := gc1(40)
	fmt.Println(ans)

}

func giftCard() func(int) int {
	initialAmount := 100
	debitFunc := func(debitAmount int) int {
		initialAmount -= debitAmount
		return initialAmount
	}
	return debitFunc
}
