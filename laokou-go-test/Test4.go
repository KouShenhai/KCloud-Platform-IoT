package main

import (
	"fmt"
	"strings"
)

func main() {
	var a = 1.5
	var b = 1
	var c bool
	c = true
	str := "3333 ddzxx"
	str = strings.Replace(str, " ", "", -1)
	fmt.Println(a, b, c, str)
}
