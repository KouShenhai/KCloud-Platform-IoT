package main

import (
	"fmt"
	"unsafe"
)

const (
	a = "anc"
	b = len(a)
	c = unsafe.Sizeof(a)
)

func main() {
	fmt.Println(a, b, c)
	const (
		dd = iota
		xx
		yy
		zz
		ddd = "333"
		xxx
		yyy = 444
		zzz = iota
		zzzz
	)
	fmt.Println(dd, xx, yy, zz, ddd, xxx, yyy, zzz, zzzz)
	const (
		i = 1 << iota
		j = 3 << iota
		x
		y
	)
	fmt.Println(i, j, x, y)
}
