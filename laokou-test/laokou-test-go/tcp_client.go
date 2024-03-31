package main

import (
	"bufio"
	"log"
	"net"
	"time"
)

func ConnectTcpServer(network string, ip net.IP, port int) net.Conn {
	conn, err := net.DialTCP(network, nil, &net.TCPAddr{
		IP:   ip,
		Port: port,
	})
	if err != nil {
		log.Printf("Client connect failed，error：%s", err)
		return nil
	}
	go func() {
		for {
			// 发送心跳
			_, err = conn.Write([]byte("heart-beat"))
			time.Sleep(5 * time.Second)
			if err != nil {
				return
			}
		}
	}()
	return conn
}

func main() {
	conn := ConnectTcpServer("tcp4", net.IPv4(127, 0, 0, 1), 7654)
	if conn != nil {
		defer func(conn net.Conn) {
			err := conn.Close()
			if err != nil {
				log.Printf("Close failed，error：%s", err)
			}
		}(conn)
		for {
			buf := make([]byte, 1024)
			reader := bufio.NewReader(conn)
			n, err := reader.Read(buf)
			if err != nil {
				return
			}
			str := string(buf[:n])
			log.Printf("Recive message：%s", str)
		}
	}
}
