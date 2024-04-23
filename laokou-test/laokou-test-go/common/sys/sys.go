package sys

import (
	"runtime"
	"strconv"
)

type Info struct {
	OS      string
	Arch    string
	CpuNum  string
	Version string
}

func GetSysInfo() Info {
	os := runtime.GOOS
	arch := runtime.GOARCH
	cpuNum := runtime.NumCPU()
	v := runtime.Version()
	return Info{OS: os, Arch: arch, CpuNum: strconv.Itoa(cpuNum), Version: v}
}
