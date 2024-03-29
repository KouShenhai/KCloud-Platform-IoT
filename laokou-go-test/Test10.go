package main

import (
	"fmt"
)

func main() {
	var a [10]int
	var i int
	for i = 0; i < 10; i++ {
		a[i] = i + 1
	}
	for i = 0; i < 10; i++ {
		fmt.Println(a[i])
	}
	fmt.Println(&i)
	var xx *int
	xx = &i
	fmt.Println(xx)
	fmt.Println(*xx)
	if xx != nil {
		fmt.Println("not nil")
	}
}
