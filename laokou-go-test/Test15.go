package main

import "fmt"

func sum(s []int, c chan int)  {
	var sum int
	for _, v := range s {
		sum += v
	}
	c <- sum
}

func main() {
	s := []int{7, 2, 8, -9, 4, 0}
	c := make(chan int)
	go sum(s[:len(s)/2],c)
	go sum(s[len(s)/2:],c)
	x,y := <- c, <- c
	fmt.Println(x,y)
}
