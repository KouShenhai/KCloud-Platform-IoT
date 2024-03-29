package main

import (
	"fmt"
)

type Book struct {
	title string
	name string
}

func main() {
	book := Book{
		"ss", "cc",
	}
	book.title = "333"
	fmt.Println(book)
}
