package main

import "fmt"

func main() {
	var maps = make(map[string]string)
	maps["ww"] = "33"
	maps["xx"] = "44"
	for key, value := range maps {
		fmt.Println(key,value)
	}
	delete(maps, "ww")
	for key, value := range maps {
		fmt.Println(key,value)
	}
}
