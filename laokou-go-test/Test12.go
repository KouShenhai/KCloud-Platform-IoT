package main

import (
	"fmt"
)

func main() {
	var arr = [5]int {1,2,3,4,5}
	fmt.Println(arr)
	fmt.Println(arr[4:5])
	fmt.Println(len(arr),cap(arr),arr)
}
