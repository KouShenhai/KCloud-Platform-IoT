package main

import (
	"bufio"
	"fmt"
	"log"
	"net"
)

func InitTcpServer(host string, port int) {
	listen, err := net.Listen("tcp", fmt.Sprintf("%s:%d", host, port))
	if err != nil {
		log.Printf("Listene failed，error：%s", err)
		return
	}
	defer func(listen net.Listener) {
		err := listen.Close()
		if err != nil {
			log.Printf("Server close failed，error：%s", err)
		}
	}(listen)
	for {
		log.Println("等待客户端连接")
		// 监听客户端
		conn, err := listen.Accept()
		if err != nil {
			log.Printf("Accept failed，error：%s", err)
			continue
		} else {
			log.Printf("Accept connect，Conn：%s，Client IP：%s", conn, conn.RemoteAddr().String())
		}
		go HandleConnect(conn)
	}
}

func HandleConnect(conn net.Conn) {
	defer func(conn net.Conn) {
		err := conn.Close()
		if err != nil {
			log.Printf("Close failed，error：%s", err)
		}
	}(conn)
	for {
		buf := make([]byte, 1024)
		// 缓冲区
		reader := bufio.NewReader(conn)
		n, err := reader.Read(buf)
		if err != nil {
			log.Printf("Read from client failed，error：%s", err)
			err := conn.Close()
			if err != nil {
				return
			}
			return
		}
		str := string(buf[:n])
		log.Printf("Recive message：%s", str)
		// 转发
		_, err = conn.Write([]byte(str))
		if err != nil {
			return
		}
	}
}

func main() {
	InitTcpServer("127.0.0.1", 7654)
}
