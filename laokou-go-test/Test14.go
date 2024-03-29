package main

import (
	"errors"
	"fmt"
)

type Employ interface {
	count() int32
}

func (c Clerk) count() int32 {
	return c.num
}

func (p Programmer) count() int32  {
	return p.num
}

type Programmer struct {
	num int32
}

type Clerk struct {
	num int32
}

func errs(f float64) (float64, error) {
	return f, errors.New("涛涛涛涛")
}

func main() {
	var e Employ
	e = Programmer{1}
	fmt.Println(e.count())
	e = Clerk{2}
	fmt.Println(e.count())
	f, err := errs(33.00)
	fmt.Println(f,err)
}
