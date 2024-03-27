package main

import (
	"fmt"
)

func main() {
	var a = 123
	var b = "xxx"
	fmt.Println(fmt.Sprintf("%s=%d", b, a))
	fmt.Printf("%s=%d", b, a)
}
