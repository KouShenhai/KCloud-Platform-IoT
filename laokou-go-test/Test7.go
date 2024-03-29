package main

import (
	"fmt"
)

func main() {
	var a = 21
	var b = 10
	var c int
	c = a + b
	fmt.Println(c)
	c = a - b
	fmt.Println(c)
	c = a / b
	fmt.Println(c)
	c = a * b
	fmt.Println(c)
	c = a % b
	fmt.Println(c)
	a++
	fmt.Println(a)
	a--
	fmt.Println(a)
	fmt.Println(a == b)
	fmt.Println(a < b)
	fmt.Println(a > b)
	fmt.Println(a <= b)
	fmt.Println(a >= b)
	var x, y = true, false
	fmt.Println(x && y)
	fmt.Println(x || y)
	var n, m = 1, 2
	fmt.Println(n >> 2)
	fmt.Println(m << 2)
	fmt.Println(m << 3)
	fmt.Println(m * 2 * 2 * 2)
	fmt.Println(64 >> 3)
	fmt.Println(64 / 2 / 2 / 2)
	// 左移动 => 2的n次平方
	// 右移动 => 2的n次开方
	var j,k = 60,13
	fmt.Println(j & k)
	fmt.Println(j | k)
	fmt.Println(j ^ k)
	fmt.Println(j << 2)
	fmt.Println(j >> 2)
}
