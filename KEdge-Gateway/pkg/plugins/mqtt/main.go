package main

import (
	"github.com/extism/go-pdk"
)

func main() {}

//go:export parse
func parse() {
	// input := pdk.InputString()
	pdk.OutputString("\t{\n\t\t\"test\": \"444\"\n\t}")
}
